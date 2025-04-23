package constant

import androidx.compose.ui.graphics.Color


object BaseResText {
    var deleteImageProConfirm: String = ""
    var errorTooLargeUpload5000: String = ""
    var errorTooLargeUpload20: String = ""
    var userNoLogin: String = ""
    var underDevelopment: String = ""
    var confirmBtn: String = ""
    var cancelBtn: String = ""
    var copyTip: String = ""
    var bgColorList: List<Color> = listOf(Color.White, Color.White, Color.White)
    var weekDayList: List<String> = listOf("", "", "", "", "", "", "", "")
}

const val RECENT_EMOJI_MAX_SIZE = 8;
const val BASE_SERVER_ADDRESS = "https://api.astercasc.com"
const val BASE_SERVER_ADDRESS_STATIC = "https://api.astercasc.com/public/resources/"
const val EMOJI_REPLACE_KEY = "EMOJI_PLACEHOLDER"
const val NETWORK_CHECK_HOST = "https://www.baidu.com"
const val MAX_TIME_SPE_SEC = 600
//val ANN_TEXT_MAP = mapOf(
//    EMOJI_REPLACE_KEY to InlineTextContent(
//        Placeholder(
//            width = 25.sp,
//            height = 25.sp,
//            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
//        )
//    ) { emoji ->
//        Image(
//            painter = painterResource(
//                biliEmojiMap[emoji] ?: Res.drawable.bili_00
//            ),
//            modifier = Modifier.fillMaxSize(),
//            contentDescription = null,
//        )
//    }
//)

//私人编码：
//Basic Multilingual Plane : U+E000 至 U+F8FF (57344 - 59647)
//Supplementary Private Use Area-A : U+F0000 至 U+FFFFD
//Supplementary Private Use Area-B： U+100000 (\uD800\uDC00) 至 U+10FFFD (\uDBFF\uDFFD)


val emojiList = listOf(
    "🤭", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇",
    "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚",
    "😋", "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔",
    "🤐", "🤨", "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "😔",
    "😪", "😴", "😷", "🤒", "🤕", "🤢", "🤮", "🤧", "😵", "🤯",
    "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😢", "😭", "😤",
    "😠", "😡", "🤬", "🤯", "😳", "🥱", "😤",
)


val kaomojiList = listOf(
    "(＾▽＾)", "(≧▽≦)", "(´▽｀)", "(๑˃ᴗ˂)ﻭ", "(￣▽￣)ノ",
    "(＊^▽^*)", "(^‿^)", "(＾ω＾)", "(o^∀^o)", "(≧ω≦)",
    "(o´▽`o)", "(＠＾－＾)", "(≧∇≦)ﾉ", "(*´▽`*)", "(＾-＾)v",
    "(ヽ*＾ω＾*)ﾉ", "(๑´ڡ`๑)", "( •̀ ω •́ )✧", "(*＾3＾)/～☆",
    "(≧∇≦)/", "(｡♥‿♥｡)", "(｡•ㅅ•｡)♡", "(｡･ω･｡)ﾉ♡", "(ฅ•ω•ฅ)♡",
    "(๑•́ ₃ •̀๑)", "(´・ω・`)", "(｡•́︿•̀｡)", "(｡♥‿♥｡)",
    "(*≧ω≦)", "(∩_∩)", "(￣3￣)", "(๑°3°๑)", "(,,• •,,)♡",
    "(｡・//ε//・｡)", "(づ￣ ³￣)づ", "(´∀｀)♡", "( ˘ ³˘)♥",
    "(๑´•.̫ • `๑)", "(づ｡◕‿‿◕｡)づ", "(´,,•ω•,,｀)♡", "(〃ω〃)",
    "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(｡･//ω//･｡)", "(*ﾉωﾉ)", "(´//ω//｀)",
    "(*ฅ́˘ฅ̀*)", "(*/ω＼*)", "(๑•́‧̫•̀๑)", "(´,,•ω•,,｀)",
    "(//▽//)", "(｡•́︿•̀｡)", "(T_T)", "(ಥ_ಥ)",
    "(இ﹏இ`｡)", "(〒︿〒)", "(╯︵╰,)", "(ಥ﹏ಥ)", "(｡•́︿•̀｡)",
    "(ノ_・。)", "(´；д；`)", "(；′⌒`)", "(つω`｡)", "(ᗒᗩᗕ)",
    "(｡•́︿•̀｡)", "(╥﹏╥)", "(ಥ_ಥ)", "｡：ﾟ(｡ﾉω＼｡)ﾟ･｡", "(つд⊂)",
    "(╥ω╥)", "(＃｀皿´)", "(￣へ￣)", "(≧ヘ≦ )", "(눈_눈)", "(¬_¬)",
    "(╬ಠ益ಠ)", "(｡•ˇ‸ˇ•｡)", "(｀⌒´メ)", "(⇀‸↼‶)", "(¬‿¬)",
    "(°ロ°) !?", "(・o・)", "(๑•̀ㅁ•́๑)", "(⊙_◎)", "Σ(°ロ°)",
    "(°ー°〃)", "Σ(￣□￣||)", "(⊙＿⊙')", "(☉｡☉)!", "(o_O)",
    "(￣ー￣)", "(￢_￢)", "(≖_≖ )", "( ´_ゝ`)", "(ಠ_ಠ)", "(；￣Д￣)",
)
