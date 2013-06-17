/* Generated By:JavaCC: Do not edit this line. Parser.java */
// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package parsers.module;

import java.util.ArrayList;
import java.util.Arrays;

import core.Coords;
import core.Function;
import core.Variable;
import core.Component;
import core.ComponentFile;

public class Parser implements ParserConstants {
  private ComponentFile _file = new ComponentFile();
  public ComponentFile getParsedFile() {
    return _file;
  }

  final public void parse() throws ParseException {
  Token moduleName;
  Token curveBracket;
  Token preposition;
  Token interfaceName;
  Token referenceName;
  Token contextName;
  Token includeName;
  Token endFactor;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INCLUDE:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(INCLUDE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INCLUDENAME:
        includeName = jj_consume_token(INCLUDENAME);
        break;
      case STRING_LITERAL:
        includeName = jj_consume_token(STRING_LITERAL);
        break;
      case NAME:
        includeName = jj_consume_token(NAME);
        break;
      case FULLNAME:
        includeName = jj_consume_token(FULLNAME);
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
     _file.includes.add(includeName.image);
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CONTEXT:
      jj_consume_token(CONTEXT);
                _file.type = Component.Type.CONTEXT;
      break;
    case MODULE:
      jj_consume_token(MODULE);
                _file.type = Component.Type.MODULE;
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    moduleName = jj_consume_token(NAME);
   _file.name = moduleName.image;
   _file.nameCoords = new Coords(moduleName.beginLine,
                                                                 moduleName.beginColumn,
                                                                 moduleName.endLine,
                                                                 moduleName.endColumn);
    curveBracket = jj_consume_token(OCB);
   _file.declarationCoords = new Coords(curveBracket.endLine,
                                                                                curveBracket.endColumn);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case USES:
      case PROVIDES:
      case TRANSITION:
      case TRIGGERS:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
     String declaration = "";
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case USES:
      case PROVIDES:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case USES:
          preposition = jj_consume_token(USES);
          break;
        case PROVIDES:
          preposition = jj_consume_token(PROVIDES);
          break;
        default:
          jj_la1[4] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTERFACE:
          jj_consume_token(INTERFACE);
          interfaceName = jj_consume_token(NAME);
            declaration = interfaceName.image;
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SEMICOLON:
            jj_consume_token(SEMICOLON);
            break;
          case AS:
            jj_consume_token(AS);
            referenceName = jj_consume_token(NAME);
             declaration += " as " + referenceName.image;
            jj_consume_token(SEMICOLON);
            break;
          default:
            jj_la1[5] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        case CONTEXTGROUP:
          jj_consume_token(CONTEXTGROUP);
          interfaceName = jj_consume_token(NAME);
            _file.usedGroups.add(interfaceName.image);
          jj_consume_token(SEMICOLON);
          break;
        default:
          jj_la1[6] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      case TRANSITION:
        preposition = jj_consume_token(TRANSITION);
          declaration = "";
        label_3:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case NAME:
            ;
            break;
          default:
            jj_la1[7] = jj_gen;
            break label_3;
          }
          contextName = jj_consume_token(NAME);
           declaration += contextName.image;
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SEMICOLON:
            endFactor = jj_consume_token(SEMICOLON);
            break;
          case COMMA:
            endFactor = jj_consume_token(COMMA);
            break;
          case IF:
            jj_consume_token(IF);
            referenceName = jj_consume_token(FULLNAME);
            declaration += " if " + referenceName.image;
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case SEMICOLON:
              endFactor = jj_consume_token(SEMICOLON);
              break;
            case COMMA:
              endFactor = jj_consume_token(COMMA);
              break;
            default:
              jj_la1[8] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
            break;
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
            if (endFactor.image.equals(",")) declaration += ",";
            else if (endFactor.image.equals(";")) break;
        }
        break;
      case TRIGGERS:
        preposition = jj_consume_token(TRIGGERS);
        contextName = jj_consume_token(FULLNAME);
          declaration = contextName.image;
        jj_consume_token(SEMICOLON);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
         if (declaration.length() > 0)
          _file.interfaces.get(preposition.image).addAll(new ArrayList<String>(Arrays.asList(declaration.split(","))));
    }
    curveBracket = jj_consume_token(CCB);
   _file.declarationCoords.setEnd(curveBracket.endLine,
                                                                  curveBracket.endColumn);
    jj_consume_token(IMPLEMENTATION);
   _file.implementationCoords = parseBody();
  }

  final public void parseFunction() throws ParseException {
  Token functionType;
  Token returnType;
  Token functionName;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EVENT:
      functionType = jj_consume_token(EVENT);
      break;
    case COMMAND:
      functionType = jj_consume_token(COMMAND);
      break;
    case LAYERED:
      functionType = jj_consume_token(LAYERED);
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   Function function = new Function();
   function.definitionCoords = new Coords(functionType.beginLine,
                                                                                  functionType.beginColumn);
    returnType = jj_consume_token(NAME);
   function.returnType = returnType.image;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      functionName = jj_consume_token(NAME);
      break;
    case FULLNAME:
      functionName = jj_consume_token(FULLNAME);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   function.name = functionName.image;
   function.definitionCoords.setEnd(functionName.endLine,
                                                                        functionName.endColumn);
    parseVars(function);
   function.bodyCoords = parseBody();
   _file.functions.get(functionType.image).add(function);
  }

  final public void parseVars(Function function) throws ParseException {
  Token varType;
  Token varLexeme;
  Token varName;
    jj_consume_token(ORB);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CRB:
      case NAME:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CRB:
        jj_consume_token(CRB);
        break;
      case NAME:
        varType = jj_consume_token(NAME);
    Variable var = new Variable();
    var.type = varType.image;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NAME:
          varName = jj_consume_token(NAME);
     var.name = varName.image;
          break;
        case LEXEME:
          varLexeme = jj_consume_token(LEXEME);
     var.lexeme = varLexeme.image;
          varName = jj_consume_token(NAME);
     var.name = varName.image;
          break;
        default:
          jj_la1[14] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          jj_consume_token(COMMA);
          break;
        case CRB:
          jj_consume_token(CRB);
          break;
        default:
          jj_la1[15] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
    function.variables.add(var);
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public Coords parseBody() throws ParseException {
  Token functionBody;
    functionBody = jj_consume_token(OCB);
   Coords coords = new Coords(functionBody.endLine,functionBody.endColumn);
   int cbcounter = 1; int rbcounter=0; String body = functionBody.image;
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LAYERED:
      case EVENT:
      case COMMAND:
      case IF:
      case SEMICOLON:
      case COMMA:
      case OCB:
      case CCB:
      case ORB:
      case CRB:
      case FULLNAME:
      case LEXEME:
      case NAME:
      case STRING_LITERAL:
      case NUMBER:
      case FLOAT:
      case FLOATING_POINT_LITERAL:
      case OPERATION:
      case ANY:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FULLNAME:
        functionBody = jj_consume_token(FULLNAME);
    body += " "+functionBody.image;
        break;
      case NAME:
        functionBody = jj_consume_token(NAME);
    body += " "+functionBody.image;
        break;
      case ORB:
        functionBody = jj_consume_token(ORB);
    body += " "+functionBody.image; rbcounter += 1;
        break;
      case CRB:
        functionBody = jj_consume_token(CRB);
    body += " "+functionBody.image; rbcounter -= 1;
        break;
      case OCB:
        functionBody = jj_consume_token(OCB);
    body += " "+functionBody.image; cbcounter += 1;
        break;
      case CCB:
        functionBody = jj_consume_token(CCB);
    body += " "+functionBody.image; cbcounter -= 1;
    if (cbcounter <= 0) {
      coords.setEnd(functionBody.beginLine,functionBody.beginColumn);
      {if (true) return coords;}
    }
        break;
      case COMMA:
        functionBody = jj_consume_token(COMMA);
    body += " "+functionBody.image;
        break;
      case SEMICOLON:
        functionBody = jj_consume_token(SEMICOLON);
    body += " "+functionBody.image;
        break;
      case STRING_LITERAL:
        functionBody = jj_consume_token(STRING_LITERAL);
    body += " "+functionBody.image;
        break;
      case LAYERED:
      case EVENT:
      case COMMAND:
        parseFunction();
        break;
      case NUMBER:
        jj_consume_token(NUMBER);
        break;
      case FLOAT:
        jj_consume_token(FLOAT);
        break;
      case FLOATING_POINT_LITERAL:
        jj_consume_token(FLOATING_POINT_LITERAL);
        break;
      case LEXEME:
        jj_consume_token(LEXEME);
        break;
      case OPERATION:
        jj_consume_token(OPERATION);
        break;
      case IF:
        jj_consume_token(IF);
        break;
      case ANY:
        functionBody = jj_consume_token(ANY);
    body += " "+functionBody.image;
        break;
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[19];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1000,0x40000000,0xa00,0x1e0000,0x60000,0x1400000,0x200400,0x0,0x3000000,0x3800000,0x1e0000,0x1c000,0x0,0x20000000,0x0,0x22000000,0x20000000,0x3f81c000,0x3f81c000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0xd,0x0,0x0,0x0,0x0,0x0,0x4,0x0,0x0,0x0,0x0,0x5,0x4,0x6,0x0,0x4,0x6ef,0x6ef,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[43];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 19; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 43; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
