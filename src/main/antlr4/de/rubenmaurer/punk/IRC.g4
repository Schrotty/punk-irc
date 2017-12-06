grammar IRC;

options
{
    language = Java;
}

/*
 * Parser Rules
 */
chat                : line+ EOF ;

line                : name command msg NEWLINE ;

msg                 : (command | WORD | WHITESPACE)+ ;

name                : WORD WHITESPACE;

reply               : identifier WHITESPACE (status (WHITESPACE asterisk)? WHITESPACE target WHITESPACE DELIMITER message | command);

command             : (WHOIS | NICK | USER | PRIVMSG | JOIN) (WHITESPACE | WORD)+ (DELIMITER message)?;

message             : (WORD | WHITESPACE)+;

identifier          : DELIMITER WORD;

status              : WORD;

target              : WORD;

asterisk            : WORD;


/*
 * Lexer Rules
 */
fragment N          : ('N'|'n') ;
fragment I          : ('I'|'i') ;
fragment C          : ('C'|'c') ;
fragment K          : ('K'|'k') ;
fragment W          : ('W'|'w') ;
fragment H          : ('H'|'h') ;
fragment O          : ('O'|'o') ;
fragment S          : ('S'|'s') ;
fragment U          : ('U'|'u') ;
fragment E          : ('E'|'e') ;
fragment R          : ('R'|'r') ;
fragment P          : ('P'|'p') ;
fragment V          : ('V'|'v') ;
fragment M          : ('M'|'m') ;
fragment G          : ('G'|'g') ;
fragment J          : ('J'|'j') ;

fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;
fragment NUMS       : [0-9] ;

NICK                : N I C K;

WHOIS               : W H O I S;

USER                : U S E R;

PRIVMSG             : P R I V M S G;

JOIN                : J O I N;

WORD                : (LOWERCASE | UPPERCASE | NUMS | '_' | '*' | '.' | '!' | '@' | '#' | '-')+ ;

DELIMITER           : ':';

WHITESPACE          : (' ' | '\t')+ ;

NEWLINE             : '\r\n';

TEXT                : ('['|'(') ~[\])]+ (']'|')');