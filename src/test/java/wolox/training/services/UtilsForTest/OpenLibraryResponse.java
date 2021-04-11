package wolox.training.services.UtilsForTest;

import com.google.common.base.Strings;
import java.util.Map;

public class OpenLibraryResponse {
    public String whenItFoundsTheBook(Map bookState){
        String isbnString= Strings.padStart(bookState.get("Isbn").toString(),10,'0');

        String string=  "{"
                + "   \"ISBN:%s\":{"
                + "      \"url\":\"https://openlibrary.org/books/OL1397864M/Zen_speaks\","
                + "      \"key\":\"/books/OL1397864M\","
                + "      \"title\":\"%s\","
                + "      \"subtitle\":\"%s\","
                + "      \"authors\":["
                + "         {"
                + "            \"url\":\"https://openlibrary.org/authors/OL223368A/Zhizhong_Cai\","
                + "            \"name\":\"%s\""
                + "         }"
                + "      ],"
                + "      \"number_of_pages\":%s,"
                + "      \"pagination\":\"159 p. :\","
                + "      \"identifiers\":{"
                + "         \"librarything\":["
                + "            \"192819\""
                + "         ],"
                + "         \"goodreads\":["
                + "            \"979250\""
                + "         ],"
                + "         \"isbn_10\":["
                + "            \"0385472579\""
                + "         ],"
                + "         \"lccn\":["
                + "            \"93005405\""
                + "         ],"
                + "         \"openlibrary\":["
                + "            \"OL1397864M\""
                + "         ]"
                + "      },"
                + "      \"classifications\":{"
                + "         \"lc_classifications\":["
                + "            \"BQ9265.6 .T7313 1994\""
                + "         ],"
                + "         \"dewey_decimal_class\":["
                + "            \"294.3/927\""
                + "         ]"
                + "      },"
                + "      \"publishers\":["
                + "         {"
                + "            \"name\":\"%s\""
                + "         }"
                + "      ],"
                + "      \"publish_places\":["
                + "         {"
                + "            \"name\":\"New York\""
                + "         }"
                + "      ],"
                + "      \"publish_date\":\"%s\","
                + "      \"subjects\":["
                + "         {"
                + "            \"name\":\"Caricatures and cartoons\","
                + "            \"url\":\"https://openlibrary.org/subjects/caricatures_and_cartoons\""
                + "         },"
                + "         {"
                + "            \"name\":\"Zen Buddhism\","
                + "            \"url\":\"https://openlibrary.org/subjects/zen_buddhism\""
                + "         }"
                + "      ],"
                + "      \"cover\":{"
                + "         \"small\":\"https://covers.openlibrary.org/b/id/240726-S.jpg\","
                + "         \"medium\":\"https://covers.openlibrary.org/b/id/240726-M.jpg\","
                + "         \"large\":\"https://covers.openlibrary.org/b/id/240726-L.jpg\""
                + "      }"
                + "   }"
                + "}";
        return String.format(string,isbnString,bookState.get("Title"),bookState.get("Subtitle"),bookState.get("Author"),bookState.get("Pages"),bookState.get("Publisher"),bookState.get("Year"));
    }
    public String whenItDoesNotFoundTheBook(){
        return "{}";
    }

}
