package constant.enums

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.drumstick_bite_solid
import tomoyo.composeapp.generated.resources.id_badge_solid
import tomoyo.composeapp.generated.resources.none
import tomoyo.composeapp.generated.resources.skull_solid
import tomoyo.composeapp.generated.resources.user_articles
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_goat
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_god
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_horse
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_loong
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_monkey
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_ox
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_pig
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_rabbit
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_rat
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_rooster
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_snake
import tomoyo.composeapp.generated.resources.user_chinese_zodiac_tiger
import tomoyo.composeapp.generated.resources.user_friends
import tomoyo.composeapp.generated.resources.user_gender_ah
import tomoyo.composeapp.generated.resources.user_gender_androgynous
import tomoyo.composeapp.generated.resources.user_gender_female
import tomoyo.composeapp.generated.resources.user_gender_male
import tomoyo.composeapp.generated.resources.user_gender_pf
import tomoyo.composeapp.generated.resources.user_gender_pm
import tomoyo.composeapp.generated.resources.user_gender_pyf
import tomoyo.composeapp.generated.resources.user_gender_pym
import tomoyo.composeapp.generated.resources.user_gender_wsb
import tomoyo.composeapp.generated.resources.user_graduate_solid
import tomoyo.composeapp.generated.resources.user_role_admin
import tomoyo.composeapp.generated.resources.user_role_alien
import tomoyo.composeapp.generated.resources.user_role_guest
import tomoyo.composeapp.generated.resources.user_role_member
import tomoyo.composeapp.generated.resources.user_role_sugar_member
import tomoyo.composeapp.generated.resources.user_role_supper_member
import tomoyo.composeapp.generated.resources.user_secret_solid
import tomoyo.composeapp.generated.resources.user_solid
import tomoyo.composeapp.generated.resources.user_thoughts
import tomoyo.composeapp.generated.resources.user_zodiac_aquarius
import tomoyo.composeapp.generated.resources.user_zodiac_aries
import tomoyo.composeapp.generated.resources.user_zodiac_cancer
import tomoyo.composeapp.generated.resources.user_zodiac_capricorn
import tomoyo.composeapp.generated.resources.user_zodiac_gemini
import tomoyo.composeapp.generated.resources.user_zodiac_leo
import tomoyo.composeapp.generated.resources.user_zodiac_libra
import tomoyo.composeapp.generated.resources.user_zodiac_pisces
import tomoyo.composeapp.generated.resources.user_zodiac_sagittarius
import tomoyo.composeapp.generated.resources.user_zodiac_scorpio
import tomoyo.composeapp.generated.resources.user_zodiac_taurus
import tomoyo.composeapp.generated.resources.user_zodiac_virgo


enum class RoleTypeEnum(
    val label: StringResource,
    val color: Color,
    val logo: DrawableResource,
    val code: Int,
) {
    ALIEN(
        Res.string.user_role_alien,
        Color(red = 94, green = 53, blue = 177),
        Res.drawable.skull_solid,
        -1
    ),
    GUEST(
        Res.string.user_role_guest,
        Color(red = 117, green = 117, blue = 117),
        Res.drawable.id_badge_solid,
        0
    ),
    MEMBER(
        Res.string.user_role_member,
        Color(red = 67, green = 160, blue = 71),
        Res.drawable.user_solid,
        1
    ),
    SUPER_MEMBER(
        Res.string.user_role_supper_member,
        Color(red = 251, green = 140, blue = 0),
        Res.drawable.user_graduate_solid,
        2
    ),
    SUGAR_MEMBER(
        Res.string.user_role_sugar_member,
        Color(red = 255, green = 179, blue = 0),
        Res.drawable.drumstick_bite_solid,
        4
    ),
    ADMIN(
        Res.string.user_role_admin,
        Color(red = 229, green = 57, blue = 53),
        Res.drawable.user_secret_solid,
        1024
    ),


    ;

    companion object {
        fun getEnumByCode(code: Int): RoleTypeEnum {
            var ret = ALIEN
            for (thisEnum in RoleTypeEnum.entries) {
                if (thisEnum.code == code) {
                    ret = thisEnum
                    break
                }
            }
            return ret
        }
    }

}


enum class GenderTypeEnum(
    val label: StringResource,
    val color: Color,
    val code: Int,
) {

    FEMALE(
        Res.string.user_gender_female,
        Color(red = 216, green = 27, blue = 96),
        0
    ),
    MALE(
        Res.string.user_gender_male,
        Color(red = 30, green = 136, blue = 229),
        1
    ),
    ANDROGYNOUS(
        Res.string.user_gender_androgynous,
        Color(red = 94, green = 53, blue = 177),
        2
    ),
    PARTIALLY_MALE_INTERSEX(
        Res.string.user_gender_pym,
        Color(red = 67, green = 160, blue = 71),
        3
    ),
    PARTIALLY_FEMALE_INTERSEX(
        Res.string.user_gender_pyf,
        Color(red = 142, green = 36, blue = 170),
        4
    ),
    PARTIAL_MALE(
        Res.string.user_gender_pm,
        Color(red = 139, green = 195, blue = 47),
        5
    ),
    PARTIAL_FEMALE(
        Res.string.user_gender_pf,
        Color(red = 0, green = 137, blue = 123),
        6
    ),
    ATTACK_HELICOPTER(
        Res.string.user_gender_ah,
        Color(red = 33, green = 33, blue = 33),
        7
    ),
    WALMART_SHOPPING_BAG(
        Res.string.user_gender_wsb,
        Color(red = 84, green = 110, blue = 122),
        8
    ),

    NONE(
        Res.string.none,
        Color(red = 0, green = 0, blue = 0),
        Int.MAX_VALUE,
    ),


    ;

    companion object {
        fun getEnumByCode(code: Int): GenderTypeEnum {
            var ret = NONE
            for (thisEnum in GenderTypeEnum.entries) {
                if (thisEnum.code == code) {
                    ret = thisEnum
                    break
                }
            }
            return ret
        }
    }

}


enum class UserZodiac(
    val label: StringResource,
    val sepDay: Int,
    val logo: String,
) {
    SAP_B(Res.string.user_zodiac_capricorn, 20, "♑"),
    AQ(Res.string.user_zodiac_aquarius, 19, "♒"),
    PI(Res.string.user_zodiac_pisces, 21, "♓"),
    AR(Res.string.user_zodiac_aries, 20, "♈"),
    TA(Res.string.user_zodiac_taurus, 21, "♉"),
    GE(Res.string.user_zodiac_gemini, 22, "♊"),
    CA(Res.string.user_zodiac_cancer, 23, "♋"),
    LE(Res.string.user_zodiac_leo, 23, "♌"),
    VI(Res.string.user_zodiac_virgo, 23, "♍"),
    LI(Res.string.user_zodiac_libra, 24, "♎"),
    SC(Res.string.user_zodiac_scorpio, 23, "♏"),
    SA(Res.string.user_zodiac_sagittarius, 22, "♐"),
    SAP_A(Res.string.user_zodiac_sagittarius, Int.MAX_VALUE, "♑"),

}

enum class UserChineseZodiac(
    val label: StringResource,
    val logo: String,
) {
    RAT(Res.string.user_chinese_zodiac_rat, "\uD83D\uDC00"),
    OX(Res.string.user_chinese_zodiac_ox, "\uD83D\uDC2E"),
    TIG(Res.string.user_chinese_zodiac_tiger, "\uD83D\uDC2F"),
    RAB(Res.string.user_chinese_zodiac_rabbit, "\uD83D\uDC07"),
    LOO(Res.string.user_chinese_zodiac_loong, "\uD83D\uDC32"),
    SN(Res.string.user_chinese_zodiac_snake, "\uD83D\uDC0D"),
    HO(Res.string.user_chinese_zodiac_horse, "\uD83D\uDC0E"),
    GOA(Res.string.user_chinese_zodiac_goat, "\uD83D\uDC0F"),
    MON(Res.string.user_chinese_zodiac_monkey, "\uD83D\uDC35"),
    RO(Res.string.user_chinese_zodiac_rooster, "\uD83D\uDC14"),
    GO(Res.string.user_chinese_zodiac_god, "\uD83D\uDC36"),
    PIG(Res.string.user_chinese_zodiac_pig, "\uD83D\uDC36"),

}


enum class UserDetailTabScreenTabModel(
    val text: StringResource,
    val collectionId: String,
) {
    FRIENDS(Res.string.user_friends, "0"),
    ARTICLE(Res.string.user_articles, "1"),
    THOUGHTS(Res.string.user_thoughts, "2"),
}