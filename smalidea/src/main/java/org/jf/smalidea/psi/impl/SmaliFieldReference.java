/*
 * Copyright 2014, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.smalidea.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jf.smalidea.SmaliTokens;
import org.jf.smalidea.psi.ElementTypes;
import org.jf.smalidea.util.PsiUtils;

public class SmaliFieldReference extends ASTWrapperPsiElement implements PsiReference {
    public SmaliFieldReference(@NotNull ASTNode node) {
        super(node);
    }

    @Override public String getName() {
        PsiElement memberName = getMemberName();
        if (memberName == null) {
            return null;
        }
        return memberName.getText();
    }

    @Override public PsiReference getReference() {
        return this;
    }

    @Override public PsiElement getElement() {
        return this;
    }

    @Override public TextRange getRangeInElement() {
        return new TextRange(0, getTextLength());
    }

    @Nullable
    public PsiClass getContainingClass() {
        SmaliClassTypeElementImpl containingClassReference = findChildByClass(SmaliClassTypeElementImpl.class);
        if (containingClassReference == null) {
            return null;
        }
        PsiClass containingClass = containingClassReference.resolve();
        if (containingClass == null) {
            return null;
        }

        return containingClass;
    }

    @Nullable
    public SmaliMemberName getMemberName() {
        return findChildByClass(SmaliMemberName.class);
    }

    @Nullable
    public ASTNode getFieldType() {
        PsiElement colonElement = findChildByType(SmaliTokens.COLON);
        return PsiUtils.findNextSibling(colonElement, ElementTypes.NONVOID_TYPE_TOKENS).getNode();
    }

    @Nullable @Override public PsiElement resolve() {
        SmaliClassTypeElementImpl containingClassReference = findChildByClass(SmaliClassTypeElementImpl.class);
        if (containingClassReference == null) {
            return null;
        }
        PsiClass containingClass = containingClassReference.resolve();
        if (containingClass == null) {
            return null;
        }

        SmaliMemberName memberName = findChildByClass(SmaliMemberName.class);
        if (memberName == null) {
            return null;
        }

        return containingClass.findFieldByName(memberName.getText(), true);
    }

    @NotNull @Override public String getCanonicalText() {
        return getText();
    }

    @Override public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        //TODO: implement this
        throw new IncorrectOperationException();
    }

    @Override public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        //TODO: implement this
        throw new IncorrectOperationException();
    }

    @Override public boolean isReferenceTo(PsiElement element) {
        return resolve() == element;
    }

    @NotNull @Override public Object[] getVariants() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    @Override public boolean isSoft() {
        return false;
    }
}
