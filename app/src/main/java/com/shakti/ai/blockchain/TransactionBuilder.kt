package com.shakti.ai.blockchain

import com.shakti.ai.models.RecordType
import org.json.JSONObject

/**
 * TransactionBuilder - Builds blockchain transactions
 * Handles transaction creation and signing
 */
class TransactionBuilder {

    fun buildStoreTransaction(
        userId: String,
        recordType: RecordType,
        data: String,
        timestamp: Long = System.currentTimeMillis()
    ): String {
        val transaction = JSONObject().apply {
            put("type", "store_record")
            put("user_id", userId)
            put("record_type", recordType.name)
            put("data", encryptData(data))
            put("timestamp", timestamp)
            put("version", "1.0")
        }

        return transaction.toString()
    }

    fun buildRetrieveTransaction(
        transactionHash: String,
        userId: String
    ): String {
        val transaction = JSONObject().apply {
            put("type", "retrieve_record")
            put("transaction_hash", transactionHash)
            put("user_id", userId)
            put("timestamp", System.currentTimeMillis())
        }

        return transaction.toString()
    }

    fun buildVerifyTransaction(transactionHash: String): String {
        val transaction = JSONObject().apply {
            put("type", "verify_record")
            put("transaction_hash", transactionHash)
            put("timestamp", System.currentTimeMillis())
        }

        return transaction.toString()
    }

    private fun encryptData(data: String): String {
        // In production, implement proper encryption
        // For now, return base64 encoded data
        return android.util.Base64.encodeToString(
            data.toByteArray(),
            android.util.Base64.NO_WRAP
        )
    }

    fun decryptData(encryptedData: String): String {
        // In production, implement proper decryption
        return try {
            String(
                android.util.Base64.decode(encryptedData, android.util.Base64.NO_WRAP)
            )
        } catch (e: Exception) {
            encryptedData
        }
    }

    fun signTransaction(transaction: String, privateKey: String): String {
        // In production, implement proper transaction signing
        // For now, return a mock signature
        return "sig_${transaction.hashCode()}"
    }

    fun validateTransaction(transaction: String): Boolean {
        return try {
            val json = JSONObject(transaction)
            json.has("type") &&
                    json.has("timestamp") &&
                    json.has("user_id")
        } catch (e: Exception) {
            false
        }
    }
}
