# Welcome to TKMacTuning

TKMacTuning aims to help you configure Mac OS X. It is written in Java/JavaFX and is published under the terms of the GNU General Public Licence version 3. I started working on TKMacTuning in 2008, but it soon got buried under a pile of other projects. Originally the app was planned to use Swing accompanied by the reference implementations of JSR-295 (Beans Binding) and JSR-296 (Swing Application Framework). You still can see that in the earliest commits. Sigh. Those were the days... Anyway, sometime I decided to do some JavaFX programming. I figured it might be a good idea to revive the project. It has grown quite a bit since then.

So, what does TKMacTuning do? 

A lot of configuration in Mac OS X is done using a defaults database. There is a commandline tool called `defaults` to access it. The command is used as follows: `defaults read com.apple.screencapture disable-shadow` displays a particular setting. `write` changes values and `delete` removes entries. TKMacTuning puts a nice user interface on top of this. So you can change settings with checkboxes, comboboxes and file dialogs. Under the hood, the commandline tool is accessed.
