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
package org.sonar.javascript.parser.statements;

import org.junit.Test;
import org.sonar.javascript.parser.JavaScriptLegacyGrammar;

import static org.sonar.javascript.utils.Assertions.assertThat;

public class StatementTest {


  @Test
  public void realLife() {
    assertThat(JavaScriptLegacyGrammar.STATEMENT)
      .matches(";")
      .matches("document.write(\"Hello world\");")
      .matches("test : doSomething();")
      .matches("break;")
      .matches("function f() {}")
      .matches("class C {}");
  }

}
