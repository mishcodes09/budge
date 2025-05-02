
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313budgetapp.Transaction
import com.example.prog3c_budgeapp.R

class ReceiptDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_detail)

        val transaction = intent.getSerializableExtra("transaction") as? Transaction

        val amountText = findViewById<TextView>(R.id.amountText)
        val storeNameText = findViewById<TextView>(R.id.storeNameText)
        val dateText = findViewById<TextView>(R.id.dateText)
        val receiptImage = findViewById<ImageView>(R.id.receiptImage)

        transaction?.let {
            amountText.text = "R${String.format("%.2f", it.amount)}"
            storeNameText.text = it.storeName
            dateText.text = it.date
            receiptImage.setImageResource(it.receiptImage)
        } ?: run {
            // Handle null transaction (fallback)
            amountText.text = "N/A"
            storeNameText.text = "Unknown"
            dateText.text = "-"
            receiptImage.setImageResource(R.drawable.download_receipt)
        }
    }
}