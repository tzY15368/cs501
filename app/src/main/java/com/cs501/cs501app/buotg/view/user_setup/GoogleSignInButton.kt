package com.cs501.cs501app.buotg.view.user_setup

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleSignInButton(onSignInSuccess: (GoogleSignInAccount) -> Unit) {
    val context = LocalContext.current
    val signInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.server_client_id))
                .requestEmail()
                .build()
        )
    }
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
        try {
            val account = task.getResult(ApiException::class.java)
            onSignInSuccess(account!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=${e.statusCode}")
            Log.w(TAG, "signInResult:failed code=${e.localizedMessage}")
        }
    }
    Button(
        modifier = Modifier
            .width(250.dp),
        shape = RoundedCornerShape(10),
        onClick = { signInLauncher.launch(signInClient.signInIntent) }
    ) {
        Text("Sign in with Google")
    }
}


@Preview
@Composable
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onSignInSuccess = { account ->
        Log.d(
            "email",
            "onSignInSuccess: ${account.email}"
        )
    })
}



