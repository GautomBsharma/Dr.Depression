package com.mksolution.depressionreducer.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mksolution.depressionreducer.Model.Article
import com.mksolution.depressionreducer.R
import com.mksolution.depressionreducer.ReadArticleActivity
import de.hdodenhof.circleimageview.CircleImageView

class ArticleAdapter(var context: Context,var list: List<Article>):
    RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {


    inner class ArticleHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val articleIm = itemView.findViewById<ImageView>(R.id.imArticle)
        val titlear = itemView.findViewById<TextView>(R.id.titleArticle)
        val article = itemView.findViewById<TextView>(R.id.articleDes)
        val readGo = itemView.findViewById<CircleImageView>(R.id.readArticle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.article_item,parent,false)
        return ArticleHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {

        val data = list[position]
        if (data.articleUrl.isNotEmpty()){
            Glide.with(context).load(data.articleUrl).into(holder.articleIm)
        }
        else{
            holder.articleIm.setImageResource(R.drawable.articlehint)
        }
        holder.article.text = data.articleDes
        holder.titlear.text = data.articleTitle
        holder.readGo.setOnClickListener {
            val intent = Intent(context,ReadArticleActivity::class.java)
            intent.putExtra("ARTICLE_URL",data.articleUrl)
            intent.putExtra("ARTICLE_TITLE",data.articleTitle)
            intent.putExtra("ARTICLE_DES",data.articleDes)

            context.startActivity(intent)
        }

    }
}