# AutoIME

Disable IME automatically when a textbox is focused. Currently works with windows only.

只在文本框被选中时启用输入法。目前只兼容Windows

Still requires [InputFix](https://github.com/zlainsama/InputFix) to allow IME to work at all though.

在lwjgl2下仍然需要[InputFix](https://github.com/zlainsama/InputFix)来允许中文输入

拒绝这很贵的！

## LICENSE

All rights reserved.
You are only granted a revocable, non-transferable and non-exclusive license to use the releases built by github action.
You are not allowed to redistribute any part of this project, be it sources or derivative works.

### Reasoning

* The project is still in a pretty bad shape - it still lacks compatibility checks with other mods.
* The project uses JNA historically, but have since transitioned to pure JNI, which will remove the need to shade jna.
* The project lacks compatibility with cocoainput, which is by far the best IME mod.
* The project only works with windows, since I have absolutely no idea what X11 and macos is doing with IME.


Open up later on when I cleared this up.


# Compat

1. InputFix. Semi-required to allow uses of IME
2. BetterQuesting.
3. BiblioCraft
4. JourneyMap
5. SteveFactoryManager (imperfect)


## Download

Currently, this is only released on github releases.

DO NOT PUBLISH THIS ON MODRINTH OR CURSEFORGE.
