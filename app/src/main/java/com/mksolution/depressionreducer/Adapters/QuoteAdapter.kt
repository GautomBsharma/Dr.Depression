package com.mksolution.depressionreducer.Adapters

import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.mksolution.depressionreducer.Model.Quote
import com.mksolution.depressionreducer.Model.QuoteDefault
import com.mksolution.depressionreducer.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class QuoteAdapter(var context: Context, var quoteList: List<Any>) : RecyclerView.Adapter<QuoteAdapter.QuoteHolder>() {

    fun updateQuotes(newQuoteList: List<Any>) {
        quoteList = newQuoteList
        notifyDataSetChanged()
    }
    // List of drawable resource IDs for backgrounds
    private val backgroundImages = listOf(
        R.drawable.quoteback1,
        R.drawable.quoteback2,
        R.drawable.quoteback3,
        R.drawable.quoteback4,
        R.drawable.quoteback5,
        R.drawable.quoteback
    )

    inner class QuoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val backQuotee = itemView.findViewById<ImageView>(R.id.backQoute)
        val textQuotee = itemView.findViewById<TextView>(R.id.tvQoute)
        val copy = itemView.findViewById<CircleImageView>(R.id.imCopy)
        val save = itemView.findViewById<CircleImageView>(R.id.imSave)
        val shave = itemView.findViewById<CircleImageView>(R.id.imShare)
        val backGround = itemView.findViewById<ConstraintLayout>(R.id.connstantFotShot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false)
        return QuoteHolder(view)
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

    override fun onBindViewHolder(holder: QuoteHolder, position: Int) {
        val quote = quoteList[position]

        if (quote is QuoteDefault) {
            holder.textQuotee.text = context.getString(quote.quoteTitle)
        } else if (quote is Quote) {

            holder.textQuotee.text = quote.quote
        }

        val randomBackground = backgroundImages.random()
        holder.backQuotee.setImageResource(randomBackground)
        holder.copy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Quote", holder.textQuotee.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        // Save the background as a screenshot
        holder.save.setOnClickListener {

            val bitmap = getScreenshot(holder.backGround, holder.save,holder.shave,holder.copy)
            saveImageToGallery(bitmap,holder.save,holder.shave,holder.copy)
        }

        // Share the background as an image
        holder.shave.setOnClickListener {

            val bitmap = getScreenshot(holder.backGround, holder.save, holder.shave, holder.copy)
            shareImage(bitmap)
        }
    }
    private fun getScreenshot(
        view: View,
        save: CircleImageView,
        shave: CircleImageView,
        copy: CircleImageView
    ): Bitmap {
        save.visibility = View.GONE
        shave.visibility = View.GONE
        copy.visibility = View.GONE
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        save.visibility = View.VISIBLE
        shave.visibility= View.VISIBLE
        copy.visibility = View.VISIBLE
        return bitmap

    }

    // Function to save image to gallery
    private fun saveImageToGallery(
        bitmap: Bitmap,
        save: CircleImageView,
        shave: CircleImageView,
        copy: CircleImageView
    ) {


        val filename = "quote_Dr.depression${System.currentTimeMillis()}.png"
        val fos: OutputStream?

        // For Android Q and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            // For devices below Android Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()

            save.visibility = View.VISIBLE
            shave.visibility= View.VISIBLE
            copy.visibility = View.VISIBLE

        }
    }

    // Function to share image via other apps
    private fun shareImage(bitmap: Bitmap) {
        val filename = "quote_share_${System.currentTimeMillis()}.png"

        // Save the image to the cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // Ensure the directory exists

        val file = File(cachePath, filename)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()

        // Get the URI for the file
        val imageUri = FileProvider.getUriForFile(context, "com.mksolution.depressionreducer", file)

        // Share the image using an Intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share image via"))
    }
}