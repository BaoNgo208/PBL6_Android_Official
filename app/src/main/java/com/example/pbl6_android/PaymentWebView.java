package com.example.pbl6_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PaymentWebView extends AppCompatActivity {
    private WebView webViewPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        String paymentUrl = getIntent().getStringExtra("paymentUrl");

        webViewPayment = findViewById(R.id.webview_payment);
        webViewPayment.setWebViewClient(new WebViewClient());
        webViewPayment.getSettings().setJavaScriptEnabled(true);

        // Load URL vào WebView
        if (paymentUrl != null) {
            webViewPayment.loadUrl(paymentUrl);
        }
    }
}