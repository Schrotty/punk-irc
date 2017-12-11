grammar IRC;

options
{
    language = Java;
}

@parser::members {
  public void reportError(RecognitionException e) {
    throw new RuntimeException("I quit!\n" + e.getMessage());
  }
}

@lexer::members {
  public void reportError(RecognitionException e) {
    throw new RuntimeException("I quit!\n" + e.getMessage());
  }
}

/*
 * Parser Rules
 */
chatLine            : (commands | WORD | WHITESPACE)+ NEWLINE?;

commands            : (nickCommand | userCommand | capCommand | quitCommand);

initCommand         : (capCommand | nickCommand | userCommand)+ NEWLINE?;

nickCommand         : 'NICK' WHITESPACE user NEWLINE?;

userCommand         : 'USER' WHITESPACE user WHITESPACE userMode WHITESPACE unused WHITESPACE DELIMITER realname NEWLINE?;

capCommand          : 'CAP LS' WHITESPACE WORD NEWLINE?;

quitCommand         : 'QUIT' WHITESPACE DELIMITER message? NEWLINE?;

/* simples */
realname            : (WORD | WHITESPACE)+;

message             : (WORD | WHITESPACE)+;

user                : WORD;

userMode            : WORD;

unused              : WORD;

/*
 * Lexer Rules
 */
fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;
fragment NUMS       : [0-9] ;

WHITESPACE          : (' ' | '\t')+ ;

WORD                : (LOWERCASE | UPPERCASE | NUMS | '_' | '*' | '.' | '!' | '@' | '#' | '-' | '?')+ ;

DELIMITER           : ':';

NEWLINE             : '\\r\\n' | '\\r' | '\r' | '\\n' | '\n' ;

TEXT                : ('['|'(') ~[\])]+ (']'|')');