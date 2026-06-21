package com.example.hexbrowser

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val outputText = findViewById<EditText>(R.id.outputText)

        val btnEncode = findViewById<Button>(R.id.btnEncode)
        val btnDecode = findViewById<Button>(R.id.btnDecode)
        val btnCopy = findViewById<Button>(R.id.btnCopy)
        val btnPaste = findViewById<Button>(R.id.btnPaste)
        val btnOpen = findViewById<Button>(R.id.btnOpen)

        btnEncode.setOnClickListener {
            val text = inputText.text.toString()
            val hex = text.toByteArray(Charsets.UTF_8)
                .joinToString("") { "%02X".format(it) }

            outputText.setText(hex)
        }

        btnDecode.setOnClickListener {
            try {
                val hex = inputText.text.toString()
                    .replace(" ", "")

                val bytes = hex.chunked(2)
                    .map { it.toInt(16).toByte() }
                    .toByteArray()

                val result = String(bytes, Charsets.UTF_8)

                outputText.setText(result)

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Hex không hợp lệ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnCopy.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            val clip =
                ClipData.newPlainText(
                    "result",
                    outputText.text.toString()
                )

            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                this,
                "Đã copy",
                Toast.LENGTH_SHORT
            ).show()
        }
        btnPaste.setOnClickListener {

            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            val text =
                clipboard.primaryClip
                    ?.getItemAt(0)
                    ?.text
                    ?.toString()

            if (!text.isNullOrEmpty()) {
                inputText.setText(text)
            }
        }
        btnOpen.setOnClickListener {

            var url = outputText.text.toString()

            if (!url.startsWith("http://")
                && !url.startsWith("https://")
            ) {
                url = "https://$url"
            }

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

                startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    "URL không hợp lệ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}