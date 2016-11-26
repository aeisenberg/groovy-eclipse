/*
 * Copyright 2009-2016 the original author or authors.
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
package org.codehaus.groovy.eclipse.refactoring.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.corext.util.JavaElementResourceMapping;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.junit.Assert;

/**
 * Copied from {@link org.eclipse.jdt.ui.tests.refactoring.ParticipantTesting}
 * Uninteresting pieces commented out
 * @created Mar 27, 2010
 */
public class ParticipantTesting {

	public static void reset() {
//		TestCreateParticipantShared.reset();
//		TestDeleteParticipantShared.reset();
//		TestMoveParticipantShared.reset();
		TestRenameParticipantShared.reset();
//		TestCopyParticipantShared.reset();

//		TestCreateParticipantSingle.reset();
//		TestDeleteParticipantSingle.reset();
//		TestMoveParticipantSingle.reset();
		TestRenameParticipantSingle.reset();
//		TestCopyParticipantSingle.reset();
	}

	public static String[] createHandles(Object object) {
		return createHandles(new Object[] { object });
	}

	public static String[] createHandles(Object obj1, Object obj2) {
		return createHandles(new Object[] { obj1, obj2 });
	}

	public static String[] createHandles(Object obj1, Object obj2, Object obj3) {
		return createHandles(new Object[] { obj1, obj2, obj3 });
	}

	public static String[] createHandles(Object obj1, Object obj2, Object obj3, Object obj4) {
		return createHandles(new Object[] { obj1, obj2, obj3, obj4 });
	}

	public static String[] createHandles(Object[] elements) {
		List result= new ArrayList();
		for (int i= 0; i < elements.length; i++) {
			Object element= elements[i];
			if (element instanceof IJavaElement) {
				result.add(((IJavaElement)element).getHandleIdentifier());
			} else if (element instanceof IResource) {
				result.add(((IResource)element).getFullPath().toString());
			} else if (element instanceof JavaElementResourceMapping) {
				result.add(((JavaElementResourceMapping)element).
					getJavaElement().getHandleIdentifier() + "_mapping");
			}
		}
		return (String[])result.toArray(new String[result.size()]);
	}

	public static void testRename(String[] expectedHandles, RenameArguments[] args) {
		Assert.assertEquals(expectedHandles.length, args.length);
		if (expectedHandles.length == 0) {
			TestRenameParticipantShared.testNumberOfElements(0);
			TestRenameParticipantSingle.testNumberOfInstances(0);
		} else {
			testElementsShared(expectedHandles, TestRenameParticipantShared.fgInstance.fHandles);
			TestRenameParticipantShared.testArguments(args);

			TestRenameParticipantSingle.testNumberOfInstances(expectedHandles.length);
			TestRenameParticipantSingle.testElements(expectedHandles);
			TestRenameParticipantSingle.testArguments(args);
		}
	}

//	public static void testMove(String[] expectedHandles, MoveArguments[] args) {
//		Assert.assertEquals(expectedHandles.length, args.length);
//		if (expectedHandles.length == 0) {
//			TestMoveParticipantShared.testNumberOfElements(0);
//			TestMoveParticipantSingle.testNumberOfInstances(0);
//		} else {
//			testElementsShared(expectedHandles, TestMoveParticipantShared.fgInstance.fHandles);
//			TestMoveParticipantShared.testArguments(args);
//
//			TestMoveParticipantSingle.testNumberOfInstances(expectedHandles.length);
//			TestMoveParticipantSingle.testElements(expectedHandles);
//			TestMoveParticipantSingle.testArguments(args);
//		}
//	}
//
//	public static void testDelete(String[] expectedHandles) {
//		if (expectedHandles.length == 0) {
//			TestDeleteParticipantShared.testNumberOfElements(0);
//			TestDeleteParticipantSingle.testNumberOfInstances(0);
//		} else {
//			testElementsShared(expectedHandles, TestDeleteParticipantShared.fgInstance.fHandles);
//
//			TestDeleteParticipantSingle.testNumberOfInstances(expectedHandles.length);
//			TestDeleteParticipantSingle.testElements(expectedHandles);
//		}
//	}
//
//	public static void testCreate(String[] expectedHandles) {
//		if (expectedHandles.length == 0)  {
//			TestCreateParticipantShared.testNumberOfElements(0);
//			TestCreateParticipantSingle.testNumberOfInstances(0);
//		} else {
//			testElementsShared(expectedHandles, TestCreateParticipantShared.fgInstance.fHandles);
//
//			TestCreateParticipantSingle.testNumberOfInstances(expectedHandles.length);
//			TestCreateParticipantSingle.testElements(expectedHandles);
//		}
//	}
//
//	public static void testCopy(String[] expectedHandles, CopyArguments[] arguments) {
//		if (expectedHandles.length == 0)  {
//			TestCopyParticipantShared.testNumberOfElements(0);
//			TestCopyParticipantSingle.testNumberOfInstances(0);
//		} else {
//			testElementsShared(expectedHandles, TestCopyParticipantShared.fgInstance.fHandles);
//			TestCopyParticipantShared.testArguments(arguments);
//
//			TestCopyParticipantSingle.testNumberOfInstances(expectedHandles.length);
//			TestCopyParticipantSingle.testElements(expectedHandles);
//			TestCopyParticipantSingle.testArguments(arguments);
//		}
//	}

	public static void testSimilarElements(List similarList, List similarNewNameList, List similarNewHandleList) {
		Assert.assertEquals(similarList.size(), similarNewNameList.size());
		if (similarList.size() == 0) {
			TestRenameParticipantShared.testNumberOfSimilarElements(0);
		} else {
			TestRenameParticipantShared.testSimilarElements(similarList, similarNewNameList, similarNewHandleList);
		}

	}

	private static void testElementsShared(String[] expected, List actual) {
		for (int i= 0; i < expected.length; i++) {
			String handle= expected[i];
			Assert.assertTrue("Expected handle not found: " + handle, actual.contains(handle));
		}
		testNumberOfElements(expected.length, actual);
	}

	private static void testNumberOfElements(int expected, List actual) {
		if (expected == 0 && actual == null)
			return;
		Assert.assertEquals(expected, actual.size());
	}
}
