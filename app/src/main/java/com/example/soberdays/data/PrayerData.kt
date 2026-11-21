package com.example.soberdays.data

import androidx.annotation.DrawableRes
import com.example.soberdays.R

data class SaintPatron(
    val name: String,
    val subtitle: String,
    val bio: String,
    @DrawableRes val imageRes: Int,
    val prayerTitle: String,
    val prayerText: String
)

data class GeneralPrayer(
    val title: String,
    val subtitle: String,
    val prayerText: String
)

object PrayerRepository {
    val patrons = listOf(
        SaintPatron(
            name = "Św. Jan Chrzciciel",
            subtitle = "Główny patron trzeźwości i abstynencji",
            bio = "Od łona matki napełniony Duchem Świętym. Całe życie praktykował surową abstynencję na pustyni, głosząc nawrócenie i prostując ścieżki dla Chrystusa.",
            imageRes = R.drawable.saint_john_baptist,
            prayerTitle = "Modlitwa do św. Jana Chrzciciela o dar trzeźwości",
            prayerText = """
                Święty Janie Chrzcicielu, który abstynencję praktykowałeś przez całe swoje życie, pomóż, abyśmy w naszej Ojczyźnie dożyli czasów, w których alkohol przestanie być symbolem nieszczęść, zmartwień i tragedii.

                Chroń nas przed grzechem upijania się. Bądź natchnieniem i pomocą dla wszystkich, którzy wyrzekają się napojów alkoholowych dla dobra swoich najbliższych i Narodu.

                Patronie trzeźwości, wzbudź w sercach moich rodaków pragnienie życia bez alkoholu, abyśmy poprzez ich abstynencję osiągnęli trzeźwość całego Narodu. Amen.
            """.trimIndent()
        ),
        SaintPatron(
            name = "Czcigodny Matt Talbot",
            subtitle = "Patron alkoholików i osób wychodzących z uzależnienia",
            bio = "Urodzony w Dublinie robotnik. Od 12. do 28. roku życia tkwił w ciężkim alkoholizmie. Z pomocą łaski Bożej podjął twarde ślubowanie trzeźwości, które utrzymał aż do śmierci, stając się wzorem dla milionów uzależnionych.",
            imageRes = R.drawable.saint_matt_talbot,
            prayerTitle = "Modlitwa o wstawiennictwo Czcigodnego Matta Talbota",
            prayerText = """
                Boże, Ty w słudze Twoim Macieju dałeś nam wspaniały przykład zwycięstwa nad nałogiem pijaństwa oraz wielkiej pokory i gorliwości w pracy.

                Udziel mi za jego przyczyną łaski i siły do walki z pokusami. Spraw, bym w chwilach słabości potrafił tak jak on zwrócić się całym sercem do Ciebie i Najświętszej Maryi Panny.

                Wspomóż wszystkich, którzy cierpią z powodu uzależnień i daj im łaskę wytrwania w trzeźwości na każdy kolejny dzień. Amen.
            """.trimIndent()
        ),
        SaintPatron(
            name = "Św. Maksymilian Maria Kolbe",
            subtitle = "Patron trzeźwości i wolności ducha",
            bio = "Franciszkanin, męczennik z Auschwitz. Niezłomnie walczył z wadami narodowymi i pijaństwem, wskazując, że prawdziwa wolność człowieka rodzi się w całkowitym oddaniu Niepokalanej.",
            imageRes = R.drawable.saint_maximilian_kolbe,
            prayerTitle = "Modlitwa do św. Maksymiliana o wolność od nałogów",
            prayerText = """
                Święty Maksymilianie, apostole trzeźwości i czystości serca, który własne życie oddałeś z miłości do brata w bunkrze głodowym!

                Uproś nam u Boga łaskę panowania nad zmysłami, odwagę do trwania w trzeźwości i uwolnienie od wszelkich zniewoleń. 

                Wstawiaj się za wszystkimi rodzinami dotkniętymi dramatem pijaństwa i broń naszą Ojczyznę przed duchem zniewolenia. Niepokalana, Matko nasza, módl się za nami! Amen.
            """.trimIndent()
        ),
        SaintPatron(
            name = "Św. Monika",
            subtitle = "Patronka matek i orędowniczka rodzin",
            bio = "Matka św. Augustyna. Przez kilkanaście lat niestrudzenie modliła się i płakała, wypraszając u Boga nawrócenie i opamiętanie dla swojego syna z jego rozwiązłego i grzesznego życia.",
            imageRes = R.drawable.saint_monica,
            prayerTitle = "Modlitwa do św. Moniki o nawrócenie i ocalenie bliskich",
            prayerText = """
                Święta Moniko, wzorze cierpliwości i niezłomnej modlitwy matczynej! Ty, która nigdy nie zwątpiłaś w miłosierdzie Boga, ratując swojego syna z sideł grzechu i zagubienia.

                Polecam Ci wszystkich, którzy przez alkohol niszczą życie własne i swoich bliskich. Daj rodzinom siłę do trwania w miłości i wyproś łaskę opamiętania, przejrzenia na oczy i wejścia na drogę trzeźwości dla każdego, kto upadł. Amen.
            """.trimIndent()
        ),
        SaintPatron(
            name = "Św. Rafał Archanioł",
            subtitle = "Boski Lekarz – patron uzdrowienia duszy i ciała",
            bio = "Jeden z siedmiu archaniołów stojących przed obliczem Boga. Jego imię oznacza „Bóg uzdrawia”. Patron uzdrowienia z ran psychicznych, duchowych i fizycznych spowodowanych nałogiem.",
            imageRes = R.drawable.saint_raphael,
            prayerTitle = "Modlitwa do św. Rafała Archanioła o uzdrowienie z nałogu",
            prayerText = """
                Święty Rafale Archaniele, Boski Przewodniku i Lekarzu dusz i ciał! 

                Przybądź z pomocą wszystkim, których serca, umysły i ciała zostały zranione przez truciznę alkoholu. Ulecz rany w relacjach, uśmierz ból samotności i wskaż pewną drogę ku wolności i zdrowiu. 

                Bądź przy mnie każdego dnia, odpędzając złe myśli i chroniąc przed upadkiem. Amen.
            """.trimIndent()
        )
    )

    val generalPrayers = listOf(
        GeneralPrayer(
            title = "Modlitwa o pogodę ducha",
            subtitle = "Codzienna modlitwa trzeźwości",
            prayerText = """
                Boże, użycz mi pogody ducha,
                abym godził się z tym, czego nie mogę zmienić,
                odwagi, abym zmieniał to, co mogę zmienić,
                i mądrości, abym odróżniał jedno od drugiego.
            """.trimIndent()
        ),
        GeneralPrayer(
            title = "Modlitwa o trzeźwość Narodu Polskiego",
            subtitle = "Błaganie przed obliczem Matki Bożej Jasnogórskiej",
            prayerText = """
                Królowo Polski, Matko Jasnogórska! Stajemy przed Twoim obliczem, zatroskani o los naszych rodzin i przyszłość Narodu. 

                Wyproś nam u Twojego Syna łaskę opamiętania i wyzwolenia z grzechu pijaństwa. Uczyń nas apostołami trzeźwości w naszych domach, miejscach pracy i całym społeczeństwie.

                Niech zmartwychwstanie w naszych sercach duch odpowiedzialności, czystości i miłości bliźniego. Amen.
            """.trimIndent()
        ),
        GeneralPrayer(
            title = "Modlitwa w chwili silnej pokusy",
            subtitle = "Awaryjna modlitwa ratunkowa",
            prayerText = """
                Panie Jezu Chryste, w tej chwili przychodzi na mnie pokusa i słabość. Moje ciało i umysł chcą uciec w stary nałóg. 

                Wierzę, że Twoja łaska jest większa niż mój głód alkoholowy. Oddaję Ci ten trudny moment, ten ból i ten niepokój. Daj mi siłę przetrwać tę godzinę i ten dzień. 

                Jezu, ufam Tobie! Maryjo, okryj mnie swoim płaszczem. Święci Patronowie trzeźwości, módlcie się za mną! Amen.
            """.trimIndent()
        )
    )
}
