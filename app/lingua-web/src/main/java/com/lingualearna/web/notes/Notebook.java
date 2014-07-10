package com.lingualearna.web.notes;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingualearna.web.security.HasOwner;
import com.lingualearna.web.security.User;
import com.lingualearna.web.validator.FieldsNotEqual;
import com.lingualearna.web.validator.Unique;

@FieldsNotEqual(propertyNames = { "foreignLang", "localLang" },
        message = "{org.lingualearna.web.validationMessages.foreignLocalLangsEqual}")
@NamedQueries({
        @NamedQuery(name = Notebook.COUNT_NOTEBOOKS_BY_NAME_QUERY, query = "SELECT count(n) FROM Notebook n WHERE n.name = :notebookName"),
        @NamedQuery(name = Notebook.FIND_ALL_QUERY, query = "SELECT n FROM Notebook n WHERE n.owner.userId = :user")
})
@Entity
@Table(name = "notebooks")
public class Notebook implements Serializable, HasOwner {

    private static final long serialVersionUID = 5569311441731774188L;

    public static final String FIND_ALL_QUERY = "Notebook.findAllByUser";
    public static final String COUNT_NOTEBOOKS_BY_NAME_QUERY = "Notebook.countNotebooksName";
    public static final String COUNT_PAGES_BY_NAME_QUERY_PARAM = "notebookName";
    public static final String USER_QUERY_PARAM = "user";

    private int notebookId;
    private Locale foreignLang;
    private Locale localLang;
    private String name;
    private User owner;
    private List<Page> pages;

    public Page addPage(Page page) {

        getPages().add(page);
        page.setNotebook(this);

        return page;
    }

    @NotNull
    @Column(name = "foreign_lang")
    public Locale getForeignLang() {

        return this.foreignLang;
    }

    @NotNull
    @Column(name = "local_lang")
    public Locale getLocalLang() {

        return this.localLang;
    }

    @NotBlank
    @Length(max = 45)
    @Unique(namedQuery = COUNT_NOTEBOOKS_BY_NAME_QUERY, valueParamName = COUNT_PAGES_BY_NAME_QUERY_PARAM,
            message = "{org.lingualearna.web.validationMessages.duplicateNotebook}")
    public String getName() {

        return this.name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notebook_id")
    public int getNotebookId() {

        return this.notebookId;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    public User getOwner() {

        return this.owner;
    }

    @JsonIgnore
    @Transient
    @Override
    public String getOwnerUsername() {

        return getOwner().getUsername();
    }

    @OrderBy("position")
    @OneToMany(mappedBy = "notebook", fetch = FetchType.EAGER)
    public List<Page> getPages() {

        return this.pages;
    }

    @Transient
    public String getUrl() {

        return "";
    }

    public Page removePage(Page page) {

        getPages().remove(page);
        page.setNotebook(null);

        return page;
    }

    public void setForeignLang(Locale foreignLang) {

        this.foreignLang = foreignLang;
    }

    public void setLocalLang(Locale localLang) {

        this.localLang = localLang;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNotebookId(int notebookId) {

        this.notebookId = notebookId;
    }

    public void setOwner(User owner) {

        this.owner = owner;
    }

    public void setPages(List<Page> pages) {

        this.pages = pages;
    }
}
