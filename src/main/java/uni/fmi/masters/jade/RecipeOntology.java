package uni.fmi.masters.jade;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import java.io.File;
import java.util.*;

public class RecipeOntology {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;

    private String ontologyIRIStr;

    public RecipeOntology() {
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        File file = new File("src/files/mydietassistant.rdf");

        try {
            ontology = manager.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            System.out.println(e.getMessage());
        }


        ontologyIRIStr = ontology.getOntologyID().getOntologyIRI().toString() + "#";
    }

    private String getClassFriendlyName(OWLClass cls) {
        String label = cls.getIRI().toString();
        label = label.substring(label.indexOf("#") + 1);

        return label;
    }



    private boolean containsSuperClass(OWLClass cls, OWLClass namedRecipe) {

       LinkedList<OWLClass> stack = new LinkedList<>();
        stack.add(cls);


        Set<OWLClass> visited = new HashSet<>();

        while (!stack.isEmpty()) {
            OWLClass current = stack.poll();
            if (visited.contains(current)) continue;
            visited.add(current);


            for (OWLClassExpression superExpr : current.getSuperClasses(ontology)) {
                if (!superExpr.isAnonymous()) {
                    OWLClass superCls = superExpr.asOWLClass();


                    if (superCls.equals(namedRecipe)) {
                        return true;
                    }
                    stack.add(superCls);
                }
            }
        }
        return false;
    }

    public ArrayList<Recipe> getRecipesByCondition(String conditionName) {
        ArrayList<Recipe> result = new ArrayList<>();

        OWLObjectProperty suitedForCondition = dataFactory.getOWLObjectProperty(IRI.create(ontologyIRIStr + "suitedForCondition"));
        OWLClass conditionClass = dataFactory.getOWLClass(IRI.create(ontologyIRIStr + conditionName));
        OWLClass namedRecipe = dataFactory.getOWLClass(IRI.create(ontologyIRIStr + "NamedRecipe"));

        for (OWLClass cls : ontology.getClassesInSignature()) {


            if (containsSuperClass(cls, namedRecipe)) {


                for (OWLAxiom ax : ontology.getAxioms(cls)) {
                    if (ax instanceof OWLSubClassOfAxiom) {
                        OWLSubClassOfAxiom subAx = (OWLSubClassOfAxiom) ax;
                        if (subAx.getSuperClass() instanceof OWLObjectSomeValuesFrom) {
                            OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) subAx.getSuperClass();

                            if (some.getProperty().equals(suitedForCondition) &&
                                    some.getFiller().equals(conditionClass)) {

                                Recipe r = new Recipe();
                                r.setName(getClassFriendlyName(cls));
                                r.setId(cls.getIRI().toString());
                                r.setIngredients(getAllIngredients(dataFactory.getOWLObjectProperty(IRI.create(ontologyIRIStr + "hasIngredient")), cls));
                                result.add(r);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    private List<String> getAllIngredients(
            OWLObjectProperty hasIngredient,
            OWLClass recipeClass) {

        List<String> ingredients = new ArrayList<>();

        for (OWLAxiom axiom : recipeClass.getReferencingAxioms(ontology)) {
            if (axiom instanceof OWLSubClassOfAxiom subAx) {
                OWLClassExpression superExpr = subAx.getSuperClass();


                if (superExpr instanceof OWLObjectSomeValuesFrom some) {


                    if (some.getProperty().equals(hasIngredient)) {

                        if (some.getFiller() instanceof OWLClass ingredientClass) {
                            ingredients.add(getClassFriendlyName(ingredientClass));
                        }
                    }
                }
            }
        }
        return ingredients;
    }

    public void renameCondition(String oldName, String newName) {
        IRI newIRI = IRI.create(ontologyIRIStr + newName);
        IRI oldIRI = IRI.create(ontologyIRIStr + oldName);
        OWLEntityRenamer renamer = new OWLEntityRenamer(manager, ontology.getImportsClosure());
        manager.applyChanges(renamer.changeIRI(oldIRI, newIRI));
        saveOntology();
    }

    private void saveOntology() {
        try {
            manager.saveOntology(ontology);
        } catch (OWLOntologyStorageException e) {
            System.out.println(e.getMessage());
        }
    }


}
