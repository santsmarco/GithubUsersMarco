package com.app.githubusersmarco.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.githubusersmarco.R
import com.app.githubusersmarco.model.UserRepository

class RepositoryAdapter(
    context: Context,
    resource: Int,
    objects: List<UserRepository>
) : ArrayAdapter<UserRepository>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_repository, parent, false)
            viewHolder = ViewHolder()
            viewHolder.descriptionRepoTextView = view.findViewById<TextView>(R.id.descriptionRepoTextView)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val repository = getItem(position)

        if (repository != null) {
            viewHolder.descriptionRepoTextView.text = repository.name
        }

        return view
    }

    private class ViewHolder {
        lateinit var descriptionRepoTextView: TextView
    }
}
