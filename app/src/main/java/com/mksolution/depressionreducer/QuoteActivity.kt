package com.mksolution.depressionreducer

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mksolution.depressionreducer.Adapters.QuoteAdapter
import com.mksolution.depressionreducer.Model.Quote
import com.mksolution.depressionreducer.Model.QuoteDefault
import com.mksolution.depressionreducer.databinding.ActivityQuoteBinding

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuoteBinding
    private var adapter: QuoteAdapter? = null
    private var quoteListDefault: ArrayList<QuoteDefault> = arrayListOf()
    private var quoteListFirebase: ArrayList<Quote> = arrayListOf()
    private var isOnline = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf(
            QuoteDefault(R.string.quote1),
            QuoteDefault(R.string.quote2),
            QuoteDefault(R.string.quote3),
            QuoteDefault(R.string.quote4),
            QuoteDefault(R.string.quote5),
            QuoteDefault(R.string.quote6)
        )
        quoteListDefault.addAll(items)

        // Set up RecyclerView with default quotes
        binding.recycleQuote.layoutManager = LinearLayoutManager(this)

        adapter = QuoteAdapter(this, quoteListDefault)
        binding.recycleQuote.adapter = adapter


        // Check if the network is available
        if (isNetworkAvailable(this)) {
            isOnline = true
            getQuoteFromFirebase()  // Load Firebase quotes
        } else {
            isOnline = false
            showNoInternetDialog()  // Show dialog if offline
            loadDefaultQuotes()      // Load default quotes
        }
    }
    // Function to check network availability
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    // Function to show default quotes when offline
    private fun loadDefaultQuotes() {
        adapter?.updateQuotes(quoteListDefault)  // Update adapter's data to default quotes
    }

    // Function to show Firebase quotes when online
    private fun getQuoteFromFirebase() {
        val reff = FirebaseDatabase.getInstance().reference.child("Quote")

        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    quoteListFirebase.clear()  // Clear the list before adding new items
                    for (snap in snapshot.children) {
                        val datt = snap.getValue(Quote::class.java)
                        datt?.let { quoteListFirebase.add(it) }
                    }

                    quoteListFirebase.reverse()
                    // Update the adapter to show Firebase quotes
                    adapter?.updateQuotes(quoteListFirebase)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    // Function to show no internet dialog
    private fun showNoInternetDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setIcon(R.drawable.round_signal_wifi_connected_no_internet_4_24)
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }
}