# Welcome to TKMacTuning

TKMacTuning aims to help you configure Mac OS X. It is written in Java/JavaFX and is published under the terms of the GNU General Public Licence version 3. I started working on TKMacTuning in 2008, but it soon got buried under a pile of other projects. Originally the app was planned to use Swing accompanied by the reference implementations of JSR-295 (Beans Binding) and JSR-296 (Swing Application Framework). You still can see that in the earliest commits. Sigh. Those were the days... Anyway, sometime I decided to do some JavaFX programming. I figured it might be a good idea to revive the project. It has grown quite a bit since then.

So, what does TKMacTuning do? 

A lot of configuration in Mac OS X is done using a defaults database. There is a commandline tool called `defaults` to access it. The command is used as follows: `defaults read com.apple.screencapture disable-shadow` displays a particular setting. `write` changes values and `delete` removes entries. TKMacTuning puts a nice user interface on top of this. So you can change settings with checkboxes, comboboxes and file dialogs. Under the hood, the commandline tool is accessed.

# Libraries

## [JSON-java](https://github.com/stleary/JSON-java)

### Licence

Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.