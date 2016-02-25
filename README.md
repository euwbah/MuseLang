# MuseLang
Programming for the Musician
=============================

Program MIDI patterns seamlessly with multiple MIDI ports.

<Still a concept! Watch this space!>

There are many live coding music platforms out there, but most of them were intended for programmers.
Herein lies my attempt to create one for the Musician instead.

(Also I do know Sonic Pi exists, but it's technically not 'for the Musician' either, as it doesn't support MIDI
out of the box.)

Inspired by the robustness of Tidal's patterns, the simplicity of Sonic Pi, and the straightforwardness of
Wulfcode.


Here are some of my brainstorms, TODOs and other thoughts while I code this behemoth (at least it is to me...)

Platform:
Java FX for GUI
javax.sound.midi for MIDI
(Hopefully I can create my out simple code highlighter/indenter thingy so I don't have to learn more APIs :P)
(oh wait yes I can!) 

Current Thoughts:
Syntax
Code Highlighting
How to get this FX thingy to actually work out

Design: currently only two code areas. One editable, the other is a console.

Syntax style: 
Whitespace are all equal 
Lexing and parsing based on function argument count 
Very basic language, no 'fundemental' type classes 

##Tokens: 
###Initial tokens
- linkport 
- listports 
- chd 
- def
- Midi Target - `<portname>:channel` 

###Pattern
- NotePattern `\......\` 
- ParamPattern `~\.....\`
- GroupEach `(....)`
- GroupWhole `[...]`
- GroupSync `{...}`
- SuffixFunc 

###

Example: 
