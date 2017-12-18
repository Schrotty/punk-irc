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

commands            : (nickCommand | userCommand | capCommand | quitCommand | privmsgCommand | ping | pong | motd | whois | notice | lusers | join);

initCommand         : (capCommand | nickCommand | userCommand)+ NEWLINE?;

nickCommand         : 'NICK' WHITESPACE delimiter? user NEWLINE?;

userCommand         : 'USER' WHITESPACE user WHITESPACE userMode WHITESPACE unused WHITESPACE DELIMITER realname NEWLINE?;

capCommand          : 'CAP LS' WHITESPACE WORD NEWLINE?;

quitCommand         : 'QUIT' WHITESPACE DELIMITER message? NEWLINE?;

privmsgCommand      : 'PRIVMSG' WHITESPACE user WHITESPACE DELIMITER message? NEWLINE?;

notice              : 'NOTICE' WHITESPACE user WHITESPACE DELIMITER message? NEWLINE?;

ping                : 'PING' NEWLINE?;

pong                : 'PONG' NEWLINE?;

motd                : 'MOTD' NEWLINE?;

whois               : 'WHOIS' WHITESPACE user NEWLINE?;

lusers              : 'LUSERS' NEWLINE?;

join                : 'JOIN' WHITESPACE WORD NEWLINE?;

/* simples */
realname            : (WORD | WHITESPACE)+;

message             : (WORD | WHITESPACE)+;

user                : WORD;

userMode            : WORD;

unused              : WORD;

delimiter           : DELIMITER;

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