package com.example.manglyextension.plugins.testsource

import com.example.manglyextension.plugins.IPreferences
import com.example.manglyextension.plugins.PreferenceUi
import com.example.manglyextension.plugins.Source
import java.util.UUID

// These can be used for things like web scraping, however this example wont use it
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


// This is a sample implementation of a source plugin for Mangly.
class TestSource(val prefs: IPreferences) : Source(prefs) {

    // Every extensions must have a unique preference key.
    // This is used as a key for the shared preferences in the main Mangly app.
    companion object {
        val preferenceKey: UUID = UUID.fromString("fb138f74-51da-4f5f-a0f3-5590f1bd5e4e")
    }

    override fun getExtensionName(): String {
        return "TestSource"
    }

    override fun generateSettings(): List<SettingGen> {
        val settingsList = mutableListOf<SettingGen>()
        settingsList.add(
            SettingGen(
                key = "testBoolean",
                defaultValue = false,
                uiElement = PreferenceUi.SWITCH, // If the UI element is empty it wont be displayed but still be used internally to storage data in the shared preferences
            )
        )

        settingsList.add(
            SettingGen(
                key = "testString",
                defaultValue = "",
                uiElement = PreferenceUi.TEXTAREA,
            )
        )

        settingsList.add(
            SettingGen(
                key = "testboolean2",
                defaultValue = true,
                uiElement = PreferenceUi.SWITCH,
            )
        )

        return settingsList

    }

    override suspend fun search(query: String): List<SearchResult> {
        val results: MutableList<SearchResult> = ArrayList()
        for (i in 1..10) {
            val title = "Test item $i" // This is the title that will be displayed for the item in the search result

            val imageUrl = "https://img.freepik.com/free-photo/abstract-bright-green-square-pixel-tile-mosaic-wall-background-texture_1258-72164.jpg" // This is the image that will be displayed above the title in the search result

            val url = "https://example.com/item$i" // This is a reference to the next page (so the page that contains all the chapters that can be selected), for this example I will work with dummy data

            // You can set custom headers for the request if needed
            val headers = mutableListOf<Header>()
            headers.add(Header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:141.0) Gecko/20100101 Firefox/141.0"))

            results.add(SearchResult(title, imageUrl, url, emptyList()))
        }

        return results
    }

    override suspend fun getImageForChaptersList(chaptersUrl: String): ImageForChaptersList {
        val headers = mutableListOf<Header>()
        headers.add(Header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:141.0) Gecko/20100101 Firefox/141.0"))

        // This is the image that will be displayed above the chapters in the chapter list
        return ImageForChaptersList(
            imageUrl = "https://img.freepik.com/free-photo/abstract-bright-green-square-pixel-tile-mosaic-wall-background-texture_1258-72164.jpg",
            headers = headers
        )
    }

    override suspend fun getChaptersFromChapterUrl(targetUrl: String): List<ChapterValue> {
        val results = mutableListOf<ChapterValue>()
        for (i in 1..10) {
            val title = "Chapter $i" // This is the title that will be displayed for the chapter in the chapter list
            val url = "$targetUrl/chapter$i" // This is a reference to the next page (so the page that contains all the images of the chapter), for this example I will work with dummy data

            results.add(ChapterValue(title, url))
        }

        return results
    }

    override suspend fun getSummary(url: String): String {
        // This is the summary that will be displayed on the chapter list screen
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies feugiat aliquet. Morbi sit amet finibus turpis. Vestibulum id pretium velit, id interdum augue. Integer condimentum nisl in tortor finibus, id porta urna luctus. Etiam ut mauris vitae nisi facilisis tincidunt. Aenean elit risus, mollis vel est eleifend, vulputate ultricies sapien. Fusce hendrerit mi ipsum, consectetur egestas nisl commodo vel."
    }

    override suspend fun getChapterImages(chapterUrl: String): ChapterImages {
        val headers = mutableListOf<Header>()
        headers.add(Header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:141.0) Gecko/20100101 Firefox/141.0"))

        // This is a list of images that will be displayed in the chapter
        val images = mutableListOf<String>()
        for (i in 1..10) {
            images.add("https://img.freepik.com/free-photo/abstract-bright-green-square-pixel-tile-mosaic-wall-background-texture_1258-72164.jpg")
        }

        return ChapterImages(images, headers)

    }

}