package constant

import androidx.compose.ui.graphics.Color
import constant.enums.MainNavigationEnum
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.bili_00
import tomoyo.composeapp.generated.resources.bili_01
import tomoyo.composeapp.generated.resources.bili_02
import tomoyo.composeapp.generated.resources.bili_03
import tomoyo.composeapp.generated.resources.bili_04
import tomoyo.composeapp.generated.resources.bili_05
import tomoyo.composeapp.generated.resources.bili_06
import tomoyo.composeapp.generated.resources.bili_07
import tomoyo.composeapp.generated.resources.bili_08
import tomoyo.composeapp.generated.resources.bili_09
import tomoyo.composeapp.generated.resources.bili_10
import tomoyo.composeapp.generated.resources.bili_11
import tomoyo.composeapp.generated.resources.bili_12
import tomoyo.composeapp.generated.resources.bili_13
import tomoyo.composeapp.generated.resources.bili_14
import tomoyo.composeapp.generated.resources.bili_15
import tomoyo.composeapp.generated.resources.bili_16
import tomoyo.composeapp.generated.resources.bili_17
import tomoyo.composeapp.generated.resources.bili_18
import tomoyo.composeapp.generated.resources.bili_19
import tomoyo.composeapp.generated.resources.bili_20
import tomoyo.composeapp.generated.resources.bili_21
import tomoyo.composeapp.generated.resources.bili_22
import tomoyo.composeapp.generated.resources.bili_23
import tomoyo.composeapp.generated.resources.bili_24
import tomoyo.composeapp.generated.resources.bili_25
import tomoyo.composeapp.generated.resources.bili_26
import tomoyo.composeapp.generated.resources.bili_27
import tomoyo.composeapp.generated.resources.bili_28
import tomoyo.composeapp.generated.resources.bili_29
import tomoyo.composeapp.generated.resources.bili_30
import tomoyo.composeapp.generated.resources.bili_31
import tomoyo.composeapp.generated.resources.bili_32
import tomoyo.composeapp.generated.resources.bili_33
import tomoyo.composeapp.generated.resources.bili_34
import tomoyo.composeapp.generated.resources.bili_35
import tomoyo.composeapp.generated.resources.bili_36
import tomoyo.composeapp.generated.resources.bili_37
import tomoyo.composeapp.generated.resources.bili_38
import tomoyo.composeapp.generated.resources.bili_39
import tomoyo.composeapp.generated.resources.bili_41
import tomoyo.composeapp.generated.resources.bili_42
import tomoyo.composeapp.generated.resources.bili_43
import tomoyo.composeapp.generated.resources.bili_44
import tomoyo.composeapp.generated.resources.bili_45
import tomoyo.composeapp.generated.resources.bili_46
import tomoyo.composeapp.generated.resources.bili_47
import tomoyo.composeapp.generated.resources.bili_48
import tomoyo.composeapp.generated.resources.bili_49
import tomoyo.composeapp.generated.resources.bili_50
import tomoyo.composeapp.generated.resources.bili_51
import tomoyo.composeapp.generated.resources.bili_52
import tomoyo.composeapp.generated.resources.bili_53
import tomoyo.composeapp.generated.resources.bili_54
import tomoyo.composeapp.generated.resources.bili_55
import tomoyo.composeapp.generated.resources.bili_56
import tomoyo.composeapp.generated.resources.bili_57
import tomoyo.composeapp.generated.resources.bili_58
import tomoyo.composeapp.generated.resources.bili_59
import tomoyo.composeapp.generated.resources.bili_60
import tomoyo.composeapp.generated.resources.bili_61
import tomoyo.composeapp.generated.resources.bili_62
import tomoyo.composeapp.generated.resources.bili_63
import tomoyo.composeapp.generated.resources.bili_64
import tomoyo.composeapp.generated.resources.bili_65
import tomoyo.composeapp.generated.resources.bili_66
import tomoyo.composeapp.generated.resources.bili_67
import tomoyo.composeapp.generated.resources.bili_68
import tomoyo.composeapp.generated.resources.bili_69
import tomoyo.composeapp.generated.resources.bili_70

const val BASE_SERVER_ADDRESS = "https://api.astercasc.com"


object BaseResText {
    var userNoLogin: String = ""
    var underDevelopment: String = ""
    var bgColorList: List<Color> = listOf(Color.White, Color.White, Color.White)
}

const val maxNumEmojiPage = 36
const val emojiReplaceKey = "EMOJI_PLACEHOLDER"
val biliEmojiMap = mapOf(
    "[#b00]" to Res.drawable.bili_00,
    "[#b01]" to Res.drawable.bili_01,
    "[#b02]" to Res.drawable.bili_02,
    "[#b03]" to Res.drawable.bili_03,
    "[#b04]" to Res.drawable.bili_04,
    "[#b05]" to Res.drawable.bili_05,
    "[#b06]" to Res.drawable.bili_06,
    "[#b07]" to Res.drawable.bili_07,
    "[#b08]" to Res.drawable.bili_08,
    "[#b09]" to Res.drawable.bili_09,
    "[#b10]" to Res.drawable.bili_10,
    "[#b11]" to Res.drawable.bili_11,
    "[#b12]" to Res.drawable.bili_12,
    "[#b13]" to Res.drawable.bili_13,
    "[#b14]" to Res.drawable.bili_14,
    "[#b15]" to Res.drawable.bili_15,
    "[#b16]" to Res.drawable.bili_16,
    "[#b17]" to Res.drawable.bili_17,
    "[#b18]" to Res.drawable.bili_18,
    "[#b19]" to Res.drawable.bili_19,
    "[#b20]" to Res.drawable.bili_20,
    "[#b21]" to Res.drawable.bili_21,
    "[#b22]" to Res.drawable.bili_22,
    "[#b23]" to Res.drawable.bili_23,
    "[#b24]" to Res.drawable.bili_24,
    "[#b25]" to Res.drawable.bili_25,
    "[#b26]" to Res.drawable.bili_26,
    "[#b27]" to Res.drawable.bili_27,
    "[#b28]" to Res.drawable.bili_28,
    "[#b29]" to Res.drawable.bili_29,
    "[#b30]" to Res.drawable.bili_30,
    "[#b31]" to Res.drawable.bili_31,
    "[#b32]" to Res.drawable.bili_32,
    "[#b33]" to Res.drawable.bili_33,
    "[#b34]" to Res.drawable.bili_34,
    "[#b35]" to Res.drawable.bili_35,
    "[#b36]" to Res.drawable.bili_36,
    "[#b37]" to Res.drawable.bili_37,
    "[#b38]" to Res.drawable.bili_38,
    "[#b39]" to Res.drawable.bili_39,
    "[#b41]" to Res.drawable.bili_41,
    "[#b42]" to Res.drawable.bili_42,
    "[#b43]" to Res.drawable.bili_43,
    "[#b44]" to Res.drawable.bili_44,
    "[#b45]" to Res.drawable.bili_45,
    "[#b46]" to Res.drawable.bili_46,
    "[#b47]" to Res.drawable.bili_47,
    "[#b48]" to Res.drawable.bili_48,
    "[#b49]" to Res.drawable.bili_49,
    "[#b50]" to Res.drawable.bili_50,
    "[#b51]" to Res.drawable.bili_51,
    "[#b52]" to Res.drawable.bili_52,
    "[#b53]" to Res.drawable.bili_53,
    "[#b54]" to Res.drawable.bili_54,
    "[#b55]" to Res.drawable.bili_55,
    "[#b56]" to Res.drawable.bili_56,
    "[#b57]" to Res.drawable.bili_57,
    "[#b58]" to Res.drawable.bili_58,
    "[#b59]" to Res.drawable.bili_59,
    "[#b60]" to Res.drawable.bili_60,
    "[#b61]" to Res.drawable.bili_61,
    "[#b62]" to Res.drawable.bili_62,
    "[#b63]" to Res.drawable.bili_63,
    "[#b64]" to Res.drawable.bili_64,
    "[#b65]" to Res.drawable.bili_65,
    "[#b66]" to Res.drawable.bili_66,
    "[#b67]" to Res.drawable.bili_67,
    "[#b68]" to Res.drawable.bili_68,
    "[#b69]" to Res.drawable.bili_69,
    "[#b70]" to Res.drawable.bili_70,
)
