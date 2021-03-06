/*
 * Copyright 2009-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.eclipse.codeassist.tests

import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.ui.PreferenceConstants
import org.eclipse.jface.text.Document
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.junit.Test

final class FieldCompletionTests extends CompletionTestSuite {

    @Test
    void testSafeDeferencing() {
        String contents = 'public class SomeClass {\nint someProperty\nvoid someMethod() { someProperty?.x}}'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '?.'))
        proposalExists(proposals, 'abs', 1)
    }

    @Test
    void testSpaces1() {
        String contents = 'public class SomeClass {\nint someProperty\nvoid someMethod() { \nnew SomeClass()    .  \n}}'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'someProperty', 1)
    }

    @Test
    void testSpaces2() {
        String contents = 'public class SomeClass {\nint someProperty\nvoid someMethod() { \nnew SomeClass()    .  \n}}'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '. '))
        proposalExists(proposals, 'someProperty', 1)
    }

    @Test
    void testSpaces3() {
        String contents = 'public class SomeClass {\nint someProperty\nvoid someMethod() { \nnew SomeClass()    .  \n}}'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '. '))
        proposalExists(proposals, 'someProperty', 1)
    }

    @Test // GRECLIPSE-616
    void testProperties1() {
        String contents = 'class Other { def x } \n new Other().x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 1)
        proposalExists(proposals, 'setX', 1)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties2() {
        String contents = 'class Other { public def x } \n new Other().x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties3() {
        String contents = 'class Other { private def x } \n new Other().x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties4() {
        String contents = 'class Other { public static final int x = 9 } \n new Other().x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties5() {
        addJavaSource('class Other { int x = 9; }', 'Other')

        String contents = 'new Other().x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    // now repeat the tests above. but with content assist on method calls instead of constructor calls

    @Test
    void testProperties1a() {
        String contents = 'class Other { def x } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 1)
        proposalExists(proposals, 'setX', 1)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties2a() {
        String contents = 'class Other { public def x } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties3a() {
        String contents = 'class Other { private def x } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties4a() {
        String contents = 'class Other { public static final int x = 9 } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test
    void testProperties5a() {
        // java class...no properties
        addJavaSource('class Other { int x = 9; }', 'Other')

        String contents = 'def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 0)
        proposalExists(proposals, 'setX', 0)
        proposalExists(proposals, 'x', 1)
    }

    @Test // GRECLIPSE-1162
    void testProperties6() {
        String contents = 'class Other { boolean x } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getX', 1)
        proposalExists(proposals, 'setX', 1)
        proposalExists(proposals, 'isX', 1)
        proposalExists(proposals, 'x', 1)
    }

    @Test // GRECLIPSE-1162
    void testProperties6a() {
        String contents = 'class Other { boolean xx } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getXx', 1)
        proposalExists(proposals, 'setXx', 1)
        proposalExists(proposals, 'isXx', 1)
        proposalExists(proposals, 'xx', 1)
    }

    @Test // GRECLIPSE-1162
    void testProperties7() {
        String contents = 'class Other { boolean isX() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'isX', 1)
        proposalExists(proposals, 'x', 1)
    }

    @Test // GRECLIPSE-1162
    void testProperties7a() {
        String contents = 'class Other { boolean isXx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'isXx', 1)
        proposalExists(proposals, 'xx', 1)
    }

    @Test // GRECLIPSE-1162
    void testProperties8() {
        String contents = 'class Other { boolean isxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'isxx', 1)
        proposalExists(proposals, 'xx', 0)
    }

    @Test // GRECLIPSE-1162
    void testProperties9() {
        String contents = 'class Other { boolean getxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getxx', 1)
        proposalExists(proposals, 'xx', 0)
    }

    @Test // GRECLIPSE-1698
    void testProperties10() {
        String contents = 'class Other { boolean getXXxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getXXxx', 1)
        proposalExists(proposals, 'XXxx', 1)
    }

    @Test // GRECLIPSE-1698
    void testProperties11() {
        String contents = 'class Other { boolean isXXxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'isXXxx', 1)
        proposalExists(proposals, 'XXxx', 1)
    }

    @Test // GRECLIPSE-1698
    void testProperties12() {
        String contents = 'class Other { boolean getxXxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'getxXxx', 1)
        proposalExists(proposals, 'xXxx', 0)
    }

    @Test // GRECLIPSE-1698
    void testProperties13() {
        String contents = 'class Other { boolean isxXxx() {} } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'isxXxx', 1)
        proposalExists(proposals, 'xXxx', 0)
    }

    @Test
    void testClosure1() {
        String contents = 'class Other { def xxx = { a, b -> }  } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        // the field
        proposalExists(proposals, 'xxx', 2)
        // the method
        proposalExists(proposals, 'xxx(Object a, Object b)', 1)
    }

    @Test
    void testClosure2() {
        String contents = 'class Other { def xxx = { int a, int b -> }  } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        // the field
        proposalExists(proposals, 'xxx', 2)
        // the method
        proposalExists(proposals, 'xxx(int a, int b)', 1)
    }

    @Test
    void testClosure3() {
        String contents = 'class Other { def xxx = { }  } \n def o = new Other()\no.x'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        // the field
        proposalExists(proposals, 'xxx', 2)
        // the method
        proposalExists(proposals, 'xxx()', 1)
    }

    @Test // GRECLIPSE-1114
    void testClosure4() {
        String contents = 'def xxx = { def bot\n b }'
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'b'))
        // from the delegate
        proposalExists(proposals, 'binding', 1)
        // from inside closure
        proposalExists(proposals, 'bot', 1)
    }

    @Test // GRECLIPSE-1114
    void testClosure5() {
        String contents = '''\
            def xxx() { }
            (0..10).each {
              xx
            }
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'xx'))
        // from the delegate
        proposalExists(proposals, 'xxx', 1)
    }

    @Test // GRECLIPSE-1114
    void testClosuret6() {
        String contents = '''\
            class Super {
              def xxx() { }
            }
            class Sub extends Super {
              def meth() {
              (0..10).each {
                xx
              }
            }
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'xx'))
        // from the delegate
        proposalExists(proposals, 'xxx', 1)
    }

    @Test // GRECLIPSE-1114
    void testClosuret7() {
        String contents = '''\
            class Super {
              def xxx
            }
            class Sub extends Super {
              def meth() {
              (0..10).each {
                xx
              }
            }
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'xx'))
        // from the delegate
        proposalExists(proposals, 'xxx', 1)
    }

    @Test // GRECLIPSE-1175
    void testInitializer1() {
        String contents = '''\
            class MyClass {
                def something = Class.
            }
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getIndexOf(contents, '.'))
        proposalExists(proposals, 'forName', 2) // two public and one private
    }

    @Test
    void testImportStaticField() {
        String contents = '''\
            import static java.util.regex.Pattern.DOTALL
            DOT
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'DOT'))
        proposalExists(proposals, 'DOTALL', 1)
    }

    @Test
    void testImportStaticStarField() {
        String contents = '''\
            import static java.util.regex.Pattern.*
            DOT
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'DOT'))
        proposalExists(proposals, 'DOTALL', 1)
    }

    @Test
    void testFavoriteStaticField() {
        setJavaPreference(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS, 'java.util.regex.Pattern.DOTALL')

        String contents = '''\
            DOT
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'DOT'))
        proposalExists(proposals, 'DOTALL', 1)

        applyProposalAndCheck(new Document(contents), findFirstProposal(proposals, 'DOTALL'), '''\
            |import static java.util.regex.Pattern.DOTALL
            |
            |DOTALL
            |'''.stripMargin())
    }

    @Test
    void testFavoriteStaticStarField() {
        setJavaPreference(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS, 'java.util.regex.Pattern.*')

        String contents = '''\
            DOT
            '''.stripIndent()
        ICompletionProposal[] proposals = createProposalsAtOffset(contents, getLastIndexOf(contents, 'DOT'))
        proposalExists(proposals, 'DOTALL', 1)

        applyProposalAndCheck(new Document(contents), findFirstProposal(proposals, 'DOTALL'), '''\
            |import static java.util.regex.Pattern.DOTALL
            |
            |DOTALL
            |'''.stripMargin())
    }
}
