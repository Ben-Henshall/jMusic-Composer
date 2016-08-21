Installation instructions:
Must have Java version 1.8+.
Run JMusicProject.jar to run the program.


About:
This program is intended as a digital musical experiement. Users may add songs to the program which will be added to a large matrix of probabilities, which acts as a means to create a new unique piece of music.
Pieces created often have sections that resemble songs put into the program while allowing the user to affect how the music is randomly generated.
A NOTE ON PITCHES: The program displays pitches using numerical values (0-128). Middle C (C4 in Pitch-class notation) is value 60, while an octave lower (C3) is 48. Each octave is 12 points above or below.

Importing Files:
You can import files by using the "Import" button on the initial screen. Currently, the program only accepts MIDI files (.mid) due to limitations of the jMusic library. Once a file is imported,
you may remove it by selecting the song you would like to remove and clicking the "remove" button. If you would like to view/edit the probability matrix, select the "View Matrix" button in the top right. If you would like to
begin generating pieces of music, click the "Generate" button in the bottom right.

Probability Matrix:
The probability matrix is a large table of probabilities, composed of the note structures of the songs you input. Each row is a Markov chain, displaying the two previous notes. The cell contains the probability of its row header being followed by its column header,
for example, if the value of the cell in row (58, 62) and column (70) is 0.5, then the chance of the note sequence (58, 62) being followed by (70) is 0.5 (50%). You are able to edit these values to change what music is generated.
If you increase the value of a cell, then the other probabilities will decrease by a proportionate amount in order to keep the total probability across a row at exactly 1.

Generating a score:
There are several customizations to the way the music is generated here. If you ever want to create a different score with the selected parameters, you can use the "Generate New Score" button at the top left of the window.
The two numerical dropdown lists, labelled "First Note" and "Second Note" are used to select which two pitches are used at the start of the new piece.
The label below this displays the total number of times this chain occured in the files you imported. You'll want this to be more than one, or the program will create a piece that continually ascends in pitch slowly, rather than being affected
by the songs you imported.
The composition structure text field can be used to structure the created piece. You enter a string of letters into the box and it will treat every unique letter as a seperate collection of notes, for example,
if the box had "AABAAC", then the bar would repeat twice (AA), then a new bar would be added (B), then the first bar would repeat twice again (AA) and then a final new bar would be added (C). For a piece that
doesn't intentionally repeat bars, you can simply enter non repeating elements such as the alphabet.
The tempo text box contains the tempo (Measures per minute) of the piece created. A normal value could range from anywhere from 40-200.
The dropdown list containing the names of instruments can be used to select which instrument is being used to play the new piece.
Alternatively, you can randomize all of these parameters by clicking on the randomize score button.
Once these parameters are set, you can click the "View Score" button to open the score in the jMusic interface. You can play the score by selecting "File -> Play MIDI" or hitting Control+P on your keyboard.
NOTE: You do not need to generate a new score when changing the tempo or instrument type. You can change these while keeping the same piece as you generated before. Changing the initial notes and composition structure, however,
requires you to generate a new score whenever you make a change.

Contact:
Ben@henshall.plus.com

MIT License

Copyright (c) [2016] [Benjamin Henshall]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.