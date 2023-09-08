package com.app.githubusersmarco.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.githubusersmarco.R
import com.app.githubusersmarco.adapter.GitHubUserAdapter
import com.app.githubusersmarco.model.GitHubUser
import com.app.githubusersmarco.service.GitHubService
import com.app.githubusersmarco.repository.GitHubRepository
import com.app.githubusersmarco.viewModel.GitHubViewModel
import com.app.githubusersmarco.factory.GitHubViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var viewModel: GitHubViewModel
    lateinit var userList: ArrayList<GitHubUser>
    lateinit var adapter: GitHubUserAdapter
    lateinit var originalUserList: ArrayList<GitHubUser>
    lateinit var progressBar: ProgressBar
    lateinit var txtLoadingUsers: TextView
    val handler = Handler(Looper.getMainLooper())
    val timeoutMillis = 15000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        val corTexto = ContextCompat.getColor(this, R.color.white)
        toolbar.setTitleTextColor(corTexto)

        progressBar = findViewById(R.id.progressBar)
        txtLoadingUsers = findViewById(R.id.textViewLoadingUsers)
        progressBar.visibility = View.VISIBLE
        txtLoadingUsers.visibility = View.VISIBLE
        listView = findViewById(R.id.listView)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val repository = GitHubRepository(service,this)

        viewModel = ViewModelProvider(this, GitHubViewModelFactory(repository))
            .get(GitHubViewModel::class.java)

        userList = ArrayList()
        originalUserList = ArrayList()
        adapter = GitHubUserAdapter(this, R.layout.item_list, userList)
        listView.adapter = adapter
        listView.divider = null
        listView.dividerHeight = 5

        startTimeoutTimer()

        viewModel.usersLiveData.observe(this) { users ->
            handler.removeCallbacksAndMessages(null)
            progressBar.visibility = View.GONE
            txtLoadingUsers.visibility = View.GONE
            originalUserList.clear()
            originalUserList.addAll(users)
            userList.clear()
            userList.addAll(users)
            adapter.notifyDataSetChanged()
        }

        viewModel.getUsers()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = userList[position]

            val intent = Intent(this, UserDetailsActivity::class.java)
            intent.putExtra("user", selectedUser)
            startActivity(intent)
        }

        val searchView = toolbar.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    userList.clear()
                    userList.addAll(originalUserList)
                    adapter.notifyDataSetChanged()
                } else {
                    val filteredList = originalUserList.filter { user ->
                        user.login.contains(newText, ignoreCase = true)
                    }
                    userList.clear()
                    userList.addAll(filteredList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }


    private fun startTimeoutTimer() {
        handler.postDelayed({
            showRetryDialog()
        }, timeoutMillis.toLong())
    }

    private fun showRetryDialog() {
        progressBar.visibility = View.GONE
        txtLoadingUsers.visibility = View.GONE
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Timeout")
        alertDialog.setMessage("A consulta demorou muito para responder. Deseja tentar novamente?")

        alertDialog.setPositiveButton("Tentar Novamente", DialogInterface.OnClickListener { _, _ ->
            viewModel.getUsers()
            progressBar.visibility = View.VISIBLE
            startTimeoutTimer()
        })

        alertDialog.show()
    }
}
