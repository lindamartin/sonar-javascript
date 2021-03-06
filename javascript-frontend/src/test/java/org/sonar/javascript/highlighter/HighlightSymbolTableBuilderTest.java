/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.javascript.highlighter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.source.Symbolizable;
import org.sonar.javascript.tree.symbols.SymbolModelImpl;
import org.sonar.javascript.utils.JavaScriptTreeModelTest;
import org.sonar.plugins.javascript.api.tree.ScriptTree;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class HighlightSymbolTableBuilderTest extends JavaScriptTreeModelTest {

  private final Symbolizable symbolizable = mock(Symbolizable.class);
  private final Symbolizable.SymbolTableBuilder symbolTableBuilder = mock(Symbolizable.SymbolTableBuilder.class);

  private static final String EOL = "\n";
  private List<String> lines;

  @Before
  public void init() {
    when(symbolizable.newSymbolTableBuilder()).thenReturn(symbolTableBuilder);
  }

  @Test
  public void sonar_symbol_table() throws Exception {
    File file = new File("src/test/resources/highlighter/symbolHighlighting.js");
    lines = Files.readLines(file, Charsets.UTF_8);
    SymbolModelImpl.create((ScriptTree) p.parse(file), symbolizable, new SourceFileOffsets(file, Charset.defaultCharset()), null);

    // variable
    verify(symbolTableBuilder).newSymbol(offset(1, 5), offset(1, 6));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(5, 1)));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(5, 7)));

    // function declaration
    verify(symbolTableBuilder).newSymbol(offset(3, 10), offset(3, 11));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(5, 5)));

    // named function expression
    verify(symbolTableBuilder).newSymbol(offset(7, 10), offset(7, 19));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(8, 10)));

    // function parameter
    verify(symbolTableBuilder).newSymbol(offset(7, 20), offset(7, 21));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(8, 20)));

    // variable with several declarations
    verify(symbolTableBuilder).newSymbol(offset(11, 5), offset(11, 6));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(13, 5)));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(12, 1)));
    verify(symbolTableBuilder).newReference(any(org.sonar.api.source.Symbol.class), eq(offset(14, 1)));

    verify(symbolTableBuilder).build();
    verifyNoMoreInteractions(symbolTableBuilder);
  }

  @Test
  public void sonar_symbol_table_built_in() throws Exception {
    File file = new File("src/test/resources/highlighter/symbolHighlightingBuiltIn.js");
    SymbolModelImpl.create((ScriptTree) p.parse(file), symbolizable, new SourceFileOffsets(file, Charset.defaultCharset()), null);

    // no offsets are used as there is uncertainty about the order of usages of built-in symbols (and first usage used for newSymbol)
    verify(symbolTableBuilder, times(3)).newSymbol(anyInt(), anyInt());
    verify(symbolTableBuilder, times(3)).newReference(any(org.sonar.api.source.Symbol.class), anyInt());

    verify(symbolTableBuilder).build();
    verifyNoMoreInteractions(symbolTableBuilder);
  }

  private int offset(int line, int column) {
    int result = 0;
    for (int i = 0; i < line - 1; i++) {
      result += lines.get(i).length() + EOL.length();
    }
    result += column - 1;
    return result;
  }
}
