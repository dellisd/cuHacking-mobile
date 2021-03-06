package com.cuhacking.app.ui.cards

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.cuhacking.app.data.api.models.SocialLinks
import org.threeten.bp.OffsetDateTime

sealed class Card {
    abstract infix fun sameAs(other: Card): Boolean

    companion object {
        val DiffCalback = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem sameAs newItem

            // All subclasses should in theory be data classes which will properly generate .equals()
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem
        }
    }
}

data class Header(@StringRes val title: Int) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? Header)?.title == title

    companion object {
        const val viewType = 1
    }
}

class Title : Card() {
    override fun sameAs(other: Card): Boolean = true

    companion object {
        const val viewType = 8
    }
}

data class CountdownCard(@StringRes val message: Int, val time: OffsetDateTime) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? CountdownCard)?.time == time

    companion object {
        const val viewType = 11

    }
}

data class UpdateCard(
    val id: String,
    val title: String,
    val description: String,
    val location: String?,
    val publishTime: OffsetDateTime
) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? UpdateCard)?.id == id

    companion object {
        const val viewType = 2
    }
}

data class WiFiCard(val ssid: String, val password: String) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? WiFiCard)?.ssid == ssid

    companion object {
        const val viewType = 3
    }
}

data class EmergencyContactCard(val number: String) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? EmergencyContactCard)?.number == number

    companion object {
        const val viewType = 99
    }
}

data class HelpCard(val message: String) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? HelpCard)?.message == message

    companion object {
        const val viewType = 101
    }
}

data class SocialCard(val socialLinks: SocialLinks) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? SocialCard)?.socialLinks == socialLinks

    companion object {
        const val viewType = 420
    }
}