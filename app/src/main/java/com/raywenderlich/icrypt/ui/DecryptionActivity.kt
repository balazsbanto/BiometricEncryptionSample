/*
 *
 *  * Copyright (c) 2020 Razeware LLC
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  * distribute, sublicense, create a derivative work, and/or sell copies of the
 *  * Software in any work that is designed, intended, or marketed for pedagogical or
 *  * instructional purposes related to programming, coding, application development,
 *  * or information technology.  Permission for such use, copying, modification,
 *  * merger, publication, distribution, sublicensing, creation of derivative works,
 *  * or sale is expressly withheld.
 *  *
 *  * This project and source code may use libraries or frameworks that are
 *  * released under various Open-Source licenses. Use of those libraries and
 *  * frameworks are governed by their own individual licenses.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 *
 */

package com.raywenderlich.icrypt.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.raywenderlich.icrypt.R
import com.raywenderlich.icrypt.common.BiometricAuthListener
import com.raywenderlich.icrypt.common.CommonUtils
import com.raywenderlich.icrypt.common.EncryptedMessage
import com.raywenderlich.icrypt.util.BiometricUtil
import com.raywenderlich.icrypt.util.CryptographyUtil
import kotlinx.android.synthetic.main.activity_decryption.*
import javax.crypto.Cipher

class DecryptionActivity : AppCompatActivity(),
    BiometricAuthListener {

  var encryptedMessage: EncryptedMessage? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_decryption)

    encryptedMessage = intent.getParcelableExtra(getString(R.string.parcel_message))
  }

  override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
    result.cryptoObject?.cipher?.let {
      decryptAndDisplay(it)
    }
  }

  override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
    CommonUtils.displayMessage(window.decorView, "Biometric error: $errorMessage")
  }

  fun onClickDecryptMessage(view: View) {
    showBiometricPromptToDecrypt()
  }

  private fun showBiometricPromptToDecrypt() {
    encryptedMessage?.initializationVector?.let { it ->
      val cryptoObject = BiometricPrompt.CryptoObject(
        CryptographyUtil.getInitializedCipherForDecryption(it)
      )

      BiometricUtil.showBiometricPrompt(
        activity = this,
        listener = this,
        cryptoObject = cryptoObject
      )
    }
  }

  private fun decryptAndDisplay(cipher: Cipher) {
    encryptedMessage?.cipherText?.let { it ->
      val decryptedMessage = CryptographyUtil.decryptData(it, cipher)
      textViewMessage.text = decryptedMessage
    }
  }



}
