package dev.lbuddyboy.flash.util

import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.*

class YamlDoc(folder: File?, private val configName: String) {
    protected var file: File
    protected var config: YamlConfiguration? = null

    init {
        file = File(folder, configName)
        init()
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully created the &6$configName &ffile."))
    }

    fun init() {
        if (!file.exists()) {
            try {
                file.createNewFile()
                loadDefaults()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        config = YamlConfiguration.loadConfiguration(file)
    }

    @Throws(IOException::class)
    fun loadDefaults() {
        val `is` = javaClass.getResourceAsStream("/$configName")
        val writer = BufferedWriter(FileWriter(file))
        writer.write(readFile(`is`))
        writer.close()
    }

    @Throws(IOException::class)
    fun readFile(inputStream: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        var content = ""
        var line: String
        while (reader.readLine().also { line = it } != null) {
            content += """
                $line
                
                """.trimIndent()
        }
        reader.close()
        return content.trim { it <= ' ' }
    }

    @Throws(IOException::class)
    fun save() {
        if (!file.exists()) file.createNewFile()
        config!!.save(file)
    }

    fun gc(): YamlConfiguration? {
        return config
    }

    @Throws(IOException::class)
    fun reloadConfig() {
        init()
    }

    companion object {
        var done = false
    }
}