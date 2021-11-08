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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.icrypt.R
import com.raywenderlich.icrypt.common.EncryptedMessage
import com.raywenderlich.icrypt.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*

class ListActivity : AppCompatActivity(), MessageListAdapter.OnItemClickListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)
    setSupportActionBar(findViewById(R.id.toolbar))

    fab.setOnClickListener { view ->
      startActivity(Intent(this, EncryptionActivity::class.java))
    }
  }

  override fun onResume() {
    super.onResume()

    // Set Adapter
    showMessageList()
  }

  override fun onItemClick(item: EncryptedMessage, itemView: View) {
    val decryptionIntent = (Intent(this, DecryptionActivity::class.java))
    decryptionIntent.putExtra(getString(R.string.parcel_message), item)
    startActivity(decryptionIntent)
  }

  private fun showMessageList() {
    val messageList = PreferenceUtil.getMessageList(applicationContext)
    if (!messageList.isNullOrEmpty()) {
      textViewNoMessage.visibility = View.GONE
      recyclerView.adapter = MessageListAdapter(messageList, this)
    }
  }

}
