This mod can help you add your own discs.
Put the ogg audio file in /.minecraft/.addurdisc/assets/addurdisc/sounds/ 
and the file name does not contain characters other than lowercase letters、numbers、-._ 
and this mod will help you add it to the game as a disc!

You can use the config file to change the directory of the resource file and whether the record can be dropped by creepers.
You can also overwrite the png file of /.minecraft/.addurdisc/assets/addurdisc/texture/item/xxx.png to customize your disc textures.

Note: The sounds resource files on the server side must be consistent！！

Tips: If your audio file is downloaded from a music app, it might contain a cover video stream, which could prevent the game from reading the audio correctly. 
You can use audio editing software (like FFmpeg) to extract the audio stream and solve this issue.

If you are using FFmpeg, you should do it like this:
ffmpeg -i input.ogg -map 0:1 -c copy output.ogg
(if 0:1 is the audio stream.)
