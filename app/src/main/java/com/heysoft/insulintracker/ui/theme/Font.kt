
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import com.heysoft.insulintracker.R


val fontProvider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val customFont = GoogleFont("Montserrat")

val myFontFamily = FontFamily(
    Font(googleFont = customFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = customFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)