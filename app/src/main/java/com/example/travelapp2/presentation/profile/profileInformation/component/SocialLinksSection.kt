package com.example.travelapp2.presentation.profile.profileInformation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@Composable
fun SocialLinksSection(
    socialLinks: List<Map<String, String>>,
    onAddLink: (String, String) -> Unit,
    onDeleteLink: (Int) -> Unit
) {
    var selectedPlatform by remember { mutableStateOf("Instagram") }
    var linkText by remember { mutableStateOf("") }
    val platforms = listOf("Instagram", "Telegram", "YouTube")

    Column {
        socialLinks.forEachIndexed { index, item ->
            val type = item["type"] ?: ""
            val url = item["url"] ?: ""

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        when (type.lowercase()) {
                            "instagram" -> R.drawable.img_instagram
                            "telegram" -> R.drawable.img_telegram
                            "youtube" -> R.drawable.img_youtube
                            else -> R.drawable.ic_locator
                        }
                    ),
                    contentDescription = type,
                    tint = Color.Unspecified,
                    modifier = Modifier.width(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = url,
                    color = colorResource(id = R.color.main),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(onClick = { onDeleteLink(index) }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Удалить")
                }
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(
                        text = selectedPlatform,
                        fontSize = 14.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        color = colorResource(id = R.color.black)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White),
                ) {
                    platforms.forEach { platform ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = platform,
                                    fontSize = 14.sp,
                                    fontFamily = Font(R.font.inter_regular).toFontFamily(),
                                    color = colorResource(id = R.color.black)
                                )
                            },
                            onClick = {
                                selectedPlatform = platform
                                expanded = false
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))


            Box(Modifier.weight(1f)) {
                CustomOutlinedField(linkText, "Ссылка") {
                    linkText = it
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = {
                    if (linkText.isNotBlank()) {
                        onAddLink(selectedPlatform, linkText)
                        linkText = ""
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    }
}