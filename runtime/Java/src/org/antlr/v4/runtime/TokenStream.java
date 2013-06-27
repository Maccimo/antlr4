/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Terence Parr
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * An {@link IntStream} whose symbols are {@link Token} instances.
 */
public interface TokenStream<Symbol> extends IntStream {
	/**
	 * Get the {@link Token} instance associated with the value returned by
	 * {@link #LA LA(k)}. This method has the same pre- and post-conditions as
	 * {@link IntStream#LA}. In addition, when the preconditions of this method
	 * are met, the return value is non-null and the value of
	 * {@code LT(k).getType()==LA(k)}.
	 *
	 * @see IntStream#LA
	 */
	@NotNull
    public Symbol LT(int k);

	/**
	 * Gets the {@link Token} at the specified {@code index} in the stream. When
	 * the preconditions of this method are met, the return value is non-null.
	 * <p/>
	 * The preconditions for this method are the same as the preconditions of
	 * {@link IntStream#seek}. If the behavior of {@code seek(index)} is
	 * unspecified for the current state and given {@code index}, then the
	 * behavior of this method is also unspecified.
	 * <p/>
	 * The symbol referred to by {@code index} differs from {@code seek()} only
	 * in the case of filtering streams where {@code index} lies before the end
	 * of the stream. Unlike {@code seek()}, this method does not adjust
	 * {@code index} to point to a non-ignored symbol.
	 *
	 * @throws IllegalArgumentException if {code index} is less than 0
	 * @throws UnsupportedOperationException if the stream does not support
	 * retrieving the token at the specified index
	 */
	@NotNull
	public Symbol get(int i);

	/**
	 * Gets the underlying {@link TokenSource} which provides tokens for this
	 * stream.
	 */
	@NotNull
	public TokenSource<? extends Symbol> getTokenSource();

	/**
	 * Return the text of all tokens within the specified {@code interval}. This
	 * method behaves like the following code (including potential exceptions
	 * for violating preconditions of {@link #get}, but may be optimized by the
	 * specific implementation.
	 *
	 * <pre>
	 * TokenStream stream = ...;
	 * String text = "";
	 * for (int i = interval.a; i <= interval.b; i++) {
	 *   text += stream.get(i).getText();
	 * }
	 * </pre>
	 *
	 * @param interval The interval of tokens within this stream to get text
	 * for.
	 * @return The text of all tokens within the specified interval in this
	 * stream.
	 *
	 * @throws NullPointerException if {@code interval} is {@code null}
	 */
	@NotNull
	public String getText(@NotNull Interval interval);

	/**
	 * Return the text of all tokens in the stream. This method behaves like the
	 * following code, including potential exceptions from the calls to
	 * {@link IntStream#size} and {@link #getText(Interval)}, but may be
	 * optimized by the specific implementation.
	 *
	 * <pre>
	 * TokenStream stream = ...;
	 * String text = stream.getText(new Interval(0, stream.size()));
	 * </pre>
	 *
	 * @return The text of all tokens in the stream.
	 */
	@NotNull
	public String getText();

	/**
	 * Return the text of all tokens in the source interval of the specified
	 * context. This method behaves like the following code, including potential
	 * exceptions from the call to {@link #getText(Interval)}, but may be
	 * optimized by the specific implementation.
	 * </p>
	 * If {@code ctx.getSourceInterval()} does not return a valid interval of
	 * tokens provided by this stream, the behavior is unspecified.
	 *
	 * <pre>
	 * TokenStream stream = ...;
	 * String text = stream.getText(ctx.getSourceInterval());
	 * </pre>
	 *
	 * @param ctx The context providing the source interval of tokens to get
	 * text for.
	 * @return The text of all tokens within the source interval of {@code ctx}.
	 */
	@NotNull
	public String getText(@NotNull RuleContext ctx);

	/**
	 * Return the text of all tokens in this stream between {@code start} and
	 * {@code stop} (inclusive).
	 * <p/>
	 * If the specified {@code start} or {@code stop} token was not provided by
	 * this stream, or if the {@code stop} occurred before the {@code start}
	 * token, the behavior is unspecified.
	 * <p/>
	 * For streams which ensure that the {@link Token#getTokenIndex} method is
	 * accurate for all of its provided tokens, this method behaves like the
	 * following code. Other streams may implement this method in other ways
	 * provided the behavior is consistent with this at a high level.
	 *
	 * <pre>
	 * TokenStream stream = ...;
	 * String text = "";
	 * for (int i = start.getTokenIndex(); i <= stop.getTokenIndex(); i++) {
	 *   text += stream.get(i).getText();
	 * }
	 * </pre>
	 *
	 * @param start The first token in the interval to get text for.
	 * @param stop The last token in the interval to get text for (inclusive).
	 * @return The text of all tokens lying between the specified {@code start}
	 * and {@code stop} tokens.
	 *
	 * @throws UnsupportedOperationException if this stream does not support
	 * this method for the specified tokens
	 */
	@NotNull
	public String getText(Object start, Object stop);
}
