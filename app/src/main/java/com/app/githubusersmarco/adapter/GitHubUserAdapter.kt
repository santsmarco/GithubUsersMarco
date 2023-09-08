package com.app.githubusersmarco.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.githubusersmarco.R
import com.app.githubusersmarco.model.GitHubUser
import com.bumptech.glide.Glide

class GitHubUserAdapter(
    context: Context,
    resource: Int,
    objects: List<GitHubUser>
) : ArrayAdapter<GitHubUser>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
            viewHolder = ViewHolder()
            viewHolder.userImageView = view.findViewById<ImageView>(R.id.userImageView)
            viewHolder.usernameTextView = view.findViewById<TextView>(R.id.userNameTextView)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val user = getItem(position)

        Glide.with(context)
            .load(user?.avatar_url)
            .placeholder(R.drawable.default_user_image)
            .into(viewHolder.userImageView)

        if (user != null) {
            viewHolder.usernameTextView.text = user.login.toUpperCase()
        }

        return view
    }

    private class ViewHolder {
        lateinit var userImageView: ImageView
        lateinit var usernameTextView: TextView
    }
}