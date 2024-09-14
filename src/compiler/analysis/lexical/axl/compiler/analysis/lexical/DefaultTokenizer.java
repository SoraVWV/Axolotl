package axl.compiler.analysis.lexical;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.utils.IllegalSourceException;
import axl.compiler.analysis.lexical.utils.TokenizerUtils;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class DefaultTokenizer implements Tokenizer, TokenizerUtils {

    @Getter
    private final IFile file;

    private int offset;

    private int line = 1;

    private int column = 1;

    private TokenizerFrame frame;

    private @Nullable Token lastToken;

    public DefaultTokenizer(@NonNull IFile file) {
        this.file = file;
        skip();
    }

    @Override
    public @NonNull Token tokenize() {
        createFrame();
        if (end())
            throw new IllegalStateException("Calling a tokenizer on a completed file");

        DefaultToken token;

        if (isIdentifierStart(peek()))
            token = readIdentifyOrKeyword();
        else if (isNumber(peek()) || peek() == '.')
            token = readNumber();
        else if (peek() == '"')
            token = readString();
        else if (peek() == '\'')
            token = readChar();
        else
            token = readDelimiterOrOperator();

        token.offset = frame.offset();
        token.column = frame.column();
        token.line = frame.line();
        token.length = this.offset - frame.offset();

        skip();
        lastToken = token;

        return token;
    }

    private @NonNull DefaultToken readString() {
        next();
        while (peek() != '"') {
            if (end())
                throw new IllegalSourceException("String literal is not completed", this, frame);

            if (peek() == '\\')
                readEscape('"');
            else
                next();
        }
        next();

        return new DefaultToken(TokenType.STRING_LITERAL);
    }

    private @NonNull DefaultToken readChar() {
        next();

        if (peek() == '\\')
            readEscape('\'');
        else
            next();

        if (next() != '\'')
            throw new IllegalSourceException("Symbol literal is not completed", this, frame);

        return new DefaultToken(TokenType.CHAR_LITERAL);
    }

    private void readEscape(char allowed) {
        TokenizerFrame localFrame = currentFrame();

        next();
        switch (peek()) {
            case '\'':
            case '"':
                if (peek() != allowed)
                    break;
            case 'n':
            case 't':
            case 'r':
            case '0':
            case '\\':
                next();
                return;
            case 'u':
                next();
                for (int i = 0; i < 4; i++) {
                    if (isHexNumber(next()))
                        continue;

                    throw new IllegalSourceException("Invalid unicode", this, localFrame);
                }
                return;
        }

        throw new IllegalSourceException("Invalid escape sequence", this, localFrame);
    }

    private @NonNull DefaultToken readIdentifyOrKeyword() {
        if (isRepresentation(TokenType.AS.getRepresentation())) {
            next(TokenType.AS.getRepresentation().length());
            return new DefaultToken(TokenType.AS);
        }

        if (isRepresentation(TokenType.IS.getRepresentation())) {
            next(TokenType.IS.getRepresentation().length());
            return new DefaultToken(TokenType.IS);
        }

        do {
            next();
        } while (isIdentifierPart(peek()));

        TokenType type = TokenType.getByRepresentation(slice());
        return new DefaultToken(type == null ? TokenType.IDENTIFY : type);
    }

    private @NonNull DefaultToken readNumber() {
        if (peek(0) == '0') {
            if (peek(1) == 'x' || peek(1) == 'X')
                return readHexNumber();
            else if (peek(1) == 'b' || peek(1) == 'B')
                return readBinNumber();
            else if (peek(1) == '.')
                return readFloatingPointNumber();
            else if (peek(1) == '_' || isNumber(peek(1)))
                return readDecNumber();

            next();
            return new DefaultToken(TokenType.DEC_NUMBER);
        }

        if (peek(0) == '.') {
            if (!isNumber(peek(1))) {
                next();
                return new DefaultToken(TokenType.DOT);
            }

            return readFloatingPointNumber();
        }

        return readDecNumber();
    }

    private DefaultToken readHexNumber() {
        next(2);
        int cnt = 0;
        boolean zeroStart = true;

        if (peek() != '0') {
            zeroStart = false;
            cnt++;
        }

        if (!isHexNumber(next()))
            throw new IllegalSourceException("Numeric literal must start with a digit", this, frame);

        boolean lastUnderscore = false;

        for (; ; ) {
            if (isHexNumber(peek())) {
                lastUnderscore = false;
                if (peek() != '0' && zeroStart) {
                    zeroStart = false;
                    cnt++;
                }
                next();
            } else if (peek() == '_') {
                lastUnderscore = true;
                next();
            } else {
                break;
            }
        }

        if (lastUnderscore)
            throw new IllegalSourceException("Numeric literal cannot have an underscore as its last character", this, frame);

        if (peek() == 'L' || peek() == 'l') {
            if (cnt > 16)
                throw new IllegalSourceException("Value of numeric literal is too large", this, frame);
            next();
            return new DefaultToken(TokenType.HEX_LONG_NUMBER);
        }

        if (cnt > 8)
            throw new IllegalSourceException("Value of numeric literal is too large", this, frame);

        return new DefaultToken(TokenType.HEX_NUMBER);
    }

    private @NonNull DefaultToken readBinNumber() {
        next(2);
        int cnt = 0;
        boolean zeroStart = true;

        if (peek() != '0') {
            zeroStart = false;
            cnt++;
        }
        if (!isBinNumber(next()))
            throw new IllegalSourceException("Numeric literal must start with a digit", this, frame);

        boolean lastUnderscore = false;

        for (; ; ) {
            if (isBinNumber(peek())) {
                lastUnderscore = false;
                if (peek() != '0' && zeroStart) {
                    zeroStart = false;
                    cnt++;
                }
                next();
            } else if (peek() == '_') {
                lastUnderscore = true;
                next();
            } else {
                break;
            }
        }

        if (lastUnderscore)
            throw new IllegalSourceException("Numeric literal cannot have an underscore as its last character", this, frame);

        if (peek() == 'L' || peek() == 'l') {
            if (cnt > 64)
                throw new IllegalSourceException("Value of numeric literal is too large", this, frame);

            next();

            return new DefaultToken(TokenType.BIN_LONG_NUMBER);
        }

        if (cnt > 32)
            throw new IllegalSourceException("Value of numeric literal is too large", this, frame);

        return new DefaultToken(TokenType.BIN_NUMBER);
    }

    private @NonNull DefaultToken readDecNumber() {
        readDecPart(true);

        if (peek() == '.' || peek() == 'E' || peek() == 'e')
            return readFloatingPointNumber();

        if (peek() == 'L' || peek() == 'l') {
            next();

            return new DefaultToken(TokenType.DEC_LONG_NUMBER);
        }

        return new DefaultToken(TokenType.DEC_NUMBER);
    }

    private @NonNull DefaultToken readFloatingPointNumber() {
        restoreFrame();

        boolean exp = false;
        readDecPart(false);

        if (peek() == '.') {
            next();
            readDecPart(true);
        }

        if (peek() == 'E' || peek() == 'e') {
            switch (next()) {
                case '-', '+':
                    next();
                default:
            }

            readDecPart(true);
            exp = true;
        }

        if (peek() == 'F' || peek() == 'f') {
            next();

            return new DefaultToken(exp ? TokenType.FLOAT_EXP_NUMBER : TokenType.FLOAT_NUMBER);
        }

        if (peek() == 'D' || peek() == 'd')
            next();

        return new DefaultToken(exp ? TokenType.DOUBLE_EXP_NUMBER : TokenType.DOUBLE_NUMBER);
    }

    private void readDecPart(boolean req) {
        boolean firstUnderscore = true;
        boolean lastUnderscore = false;
        boolean hasNumber = false;

        for (; ; ) {
            if (isNumber(peek())) {
                lastUnderscore = false;
                firstUnderscore = false;
                hasNumber = true;
                next();
            } else if (peek() == '_') {
                if (firstUnderscore)
                    throw new IllegalSourceException("Numeric literal cannot have an underscore as it's first or last character", this, frame);

                lastUnderscore = true;
                next();
            } else {
                break;
            }
        }

        if (!hasNumber && req)
            throw new IllegalSourceException("Numeric literal cannot terminate with a dot", this, frame);

        if (lastUnderscore)
            throw new IllegalSourceException("Numeric literal cannot have an underscore as it's last character", this, frame);
    }

    // TODO optimization
    private @NonNull DefaultToken readDelimiterOrOperator() {
        int currentLength = 0;
        TokenType current = null;

        for (TokenType type : TokenType.delimitersAndOperators()) {
            String representation = type.getRepresentation();
            if (!isRepresentation(representation))
                continue;

            if (currentLength < representation.length()) {
                currentLength = representation.length();
                current = type;
            }
        }

        if (current == null)
            throw new IllegalSourceException("Unknown symbol", this, frame);

        if (current == TokenType.MINUS) {
            if (
                    lastToken == null ||
                            lastToken.getType().getGroup() == TokenGroup.OPERATOR ||
                            lastToken.getType() == TokenType.LEFT_PARENT ||
                            lastToken.getType() == TokenType.LEFT_SQUARE ||
                            lastToken.getType() == TokenType.RETURN ||
                            lastToken.getType() == TokenType.THIS
            )
                current = TokenType.UNARY_MINUS;
        }

        next(currentLength);
        return new DefaultToken(current);
    }

    private boolean isRepresentation(String representation) {
        for (int i = 0; i < representation.length(); i++)
            if (peek(i) != representation.charAt(i))
                return false;

        return true;
    }

    private void skip() {
        for (; ; ) {
            if (peek(0) == '/' && peek(1) == '*')
                readMultilineComment();
            else if (peek(0) == '/' && peek(1) == '/')
                readSingleComment();
            else if (peek() == ' ' || peek() == '\t' || peek() == '\n' || peek() == '\r')
                next();
            else
                break;
        }
    }

    private void readSingleComment() {
        next(2);

        while (peek() != '\r' && peek() != '\n' && peek() != '\0')
            next();

        next();
    }

    private void readMultilineComment() {
        TokenizerFrame localFrame = currentFrame();
        next(2);

        while (peek(0) != '*' || peek(1) != '/') {
            if (end())
                throw new IllegalSourceException("Multiline comment was not closed", this, localFrame);

            next();
        }

        next(2);
    }

    private void next(int n) {
        for (int i = 0; i < n; i++)
            next();
    }

    private char next() {
        char result = peek();
        if (result == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        offset++;

        return result;
    }

    private char peek() {
        return peek(0);
    }

    private char peek(int n) {
        int offset = this.offset + n;
        if (offset >= file.getContent().length())
            return '\0';

        return file.getContent().charAt(offset);
    }

    private boolean end() {
        return offset >= file.getContent().length();
    }

    private @NonNull String slice() {
        return getFile()
                .getContent()
                .substring(
                        frame.offset(),
                        this.offset
                );
    }

    private void createFrame() {
        this.frame = currentFrame();
    }

    private void restoreFrame() {
        this.offset = frame.offset();
        this.column = frame.column();
        this.line = frame.line();
    }

    private TokenizerFrame currentFrame() {
        return new DefaultTokenizerFrame(offset, line, column);
    }

    @Override
    public boolean isProcessed() {
        return end();
    }
}
