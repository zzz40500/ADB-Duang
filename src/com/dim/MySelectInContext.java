package com.dim;

import com.intellij.ide.FileEditorProvider;
import com.intellij.ide.SelectInContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Created by Luonanqin on 3/1/16.
 */
public class MySelectInContext implements SelectInContext {
    @NotNull
    private final VirtualFile targetFile;
    @Nullable
    private final Editor editor;
    @Nullable
    private final Project project;

    public MySelectInContext(@NotNull VirtualFile targetFile, @Nullable Editor editor, Project project) {
        this.targetFile = targetFile;
        this.editor = editor;
        this.project = project;
    }

    @Override
    @NotNull
    public Project getProject() {
        return project;
    }

    @NotNull
    private PsiFile getPsiFile() {
        return PsiManager.getInstance(project).findFile(targetFile);
    }

    @Override
    @NotNull
    public FileEditorProvider getFileEditorProvider() {
        return null;
    }

    @NotNull
    private PsiElement getPsiElement() {
        PsiElement e = null;
        if (editor != null) {
            final int offset = editor.getCaretModel().getOffset();
            PsiDocumentManager.getInstance(project).commitAllDocuments();
            e = getPsiFile().findElementAt(offset);
        }
        if (e == null) {
            e = getPsiFile();
        }
        return e;
    }

    @Override
    @NotNull
    public VirtualFile getVirtualFile() {
        return targetFile;
    }

    @Override
    public Object getSelectorInFile() {
        return getPsiElement();
    }
}