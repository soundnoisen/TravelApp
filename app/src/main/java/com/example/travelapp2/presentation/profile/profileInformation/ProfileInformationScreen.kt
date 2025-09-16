package com.example.travelapp2.presentation.profile.profileInformation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.emptyUserPhoto
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.profile.profileInformation.component.CustomOutlinedField
import com.example.travelapp2.presentation.profile.profileInformation.component.DataRow
import com.example.travelapp2.presentation.profile.profileInformation.component.ProfileInformationHeader
import com.example.travelapp2.presentation.profile.profileInformation.component.SocialLinksSection
import com.example.travelapp2.presentation.profile.profileInformation.component.UserEditPhoto
import com.example.travelapp2.presentation.util.uploadToCloudinary
import com.example.travelapp2.presentation.util.uriToFile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ProfileInformationScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    userId: String,
    mainNavController: NavHostController
) {
    val context = LocalContext.current

    LaunchedEffect(userId) {
        userProfileViewModel.loadUserProfile(userId)
    }

    val profile by userProfileViewModel.profile.observeAsState()

    var id by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var socialLinks by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var email by remember { mutableStateOf("") }

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }



    LaunchedEffect(profile) {
        profile?.let {
            id = it.userId
            displayName = it.displayName
            photoUrl = it.photoUrl
            bio = it.bio
            socialLinks = it.socialLinks
            email = it.email
        }
    }

    val scope = rememberCoroutineScope()

    fun onSavePressed() {
        scope.launch {

            selectedPhotoUri?.let { uri ->
                val file = uriToFile(uri, context)
                file?.let {
                    val uploaded = uploadToCloudinary(it)
                    if (uploaded != null) {
                        photoUrl = uploaded
                    } else {
                        showToast(context, "Не удалось загрузить фото")
                    }
                }
            }

            val userProfile = UserProfileFirebase(
                userId = id,
                displayName = displayName,
                bio = bio,
                socialLinks = socialLinks,
                photoUrl = photoUrl
            )

            userProfileViewModel.updateUserProfile(userProfile)

            navController.popBackStack()
        }
    }

    if (profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        }
    } else {

        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background)),
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            ProfileInformationHeader(navController, mainNavController, userProfileViewModel)

            Column(Modifier.fillMaxSize()
                .padding(horizontal = 16.dp)) {


                Box(
                    Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    UserEditPhoto(
                        selectedPhotoUri = selectedPhotoUri,
                        photoUrl = photoUrl
                    ) { uri ->
                        selectedPhotoUri = uri
                    }


                }


                Spacer(Modifier.height(16.dp))
                CustomText("Имя в профиле", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = displayName,
                    placeholder = "Введите ваши имя и фамилию",
                    onTextChanged = { input -> displayName = input },
                )

                Spacer(Modifier.height(16.dp))
                CustomText("О себе", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = bio,
                    placeholder = "Напишите о себе",
                    onTextChanged = { input -> bio = input },
                )


                Spacer(Modifier.height(16.dp))
                CustomText("Ваши социальные сети", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                SocialLinksSection(
                    socialLinks = socialLinks,
                    onAddLink = { platform, url ->
                        socialLinks = socialLinks + mapOf("type" to platform, "url" to url)
                    },
                    onDeleteLink = { index ->
                        socialLinks = socialLinks.toMutableList().also { it.removeAt(index) }
                    }
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Данные для авторизации", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                DataRow(R.drawable.ic_email, email) {}
                DataRow(R.drawable.ic_password2, "Пароль") {}
                Spacer(modifier = Modifier.weight(1f))
                CustomButton(onClick = { onSavePressed() }, buttonText = "Сохранить")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}









