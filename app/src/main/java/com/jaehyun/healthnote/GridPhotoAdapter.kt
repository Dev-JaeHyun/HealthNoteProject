package com.jaehyun.healthnote

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jaehyun.healthnote.dataclass.EncodingImage

class GridPhotoAdapter(private var context: Context?, private var photoList: ArrayList<EncodingImage>) : BaseAdapter(){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val photo : EncodingImage = photoList[position]
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val photoView : View = inflater.inflate(R.layout.item_gridphoto, null)

        photoView.findViewById<ImageView>(R.id.photo).setImageBitmap(decodePicString(photo.base64EncodingImage))
        photoView.findViewById<TextView>(R.id.likeCount).text = photo.goodCount.toString()

        return photoView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return photoList.size
    }
    fun decodePicString (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
    fun resizeBitmap(original: Bitmap): Bitmap? {
        val resizeWidth = 1080
        val aspectRatio = original.height.toDouble() / original.width.toDouble()
        val targetHeight = (resizeWidth * aspectRatio).toInt()
        val result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false)
        if (result != original) {
            original.recycle()
        }
        return result
    }

}