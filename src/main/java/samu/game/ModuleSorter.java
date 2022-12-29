package samu.game;

import java.util.*;

class ModuleSorter<G extends Game<G>> {
    private final Map<Module<? extends G>, List<Module<? extends G>>> relations = new HashMap<>();
    private final Set<NSID> circularDependencies = new TreeSet<>();
    private final Set<NSID> missing = new TreeSet<>();
    private final Set<NSID> optMissing = new TreeSet<>();
    private final Set<NSID> loaded = new TreeSet<>();
    private final List<Module<? extends G>> modules = new ArrayList<>();

    private final Map<NSID, Module<? extends G>> moduleMap;

    ModuleSorter(Map<NSID, Module<? extends G>> moduleMap) {
        this.moduleMap = moduleMap;
    }

    private void relate(Module<? extends G> before, Module<? extends G> after) {
        relations.computeIfAbsent(after, k -> new ArrayList<>()).add(before);
    }

    private void setupRelations() {
        for (Module<? extends G> module : moduleMap.values()) {
            for (Map.Entry<NSID, Relation> entry : module.before.entrySet()) {
                NSID dep = entry.getKey();
                Relation rel = entry.getValue();

                Module<? extends G> depmod = moduleMap.get(dep);
                if (depmod == null) {
                    if (rel == Relation.REQUIRED)
                        missing.add(dep);
                    else
                        optMissing.add(dep);
                    continue;
                }

                relate(module, depmod);
            }
            for (Map.Entry<NSID, Relation> entry : module.after.entrySet()) {
                NSID dep = entry.getKey();
                Relation rel = entry.getValue();

                Module<? extends G> depmod = moduleMap.get(dep);
                if (depmod == null) {
                    if (rel == Relation.REQUIRED)
                        missing.add(dep);
                    else
                        optMissing.add(dep);
                    continue;
                }

                relate(depmod, module);
            }
        }
    }

    private void reorder() {
        Stack<Module<? extends G>> stack = new Stack<>();
        Set<Module<? extends G>> toBeAdded = new HashSet<>(moduleMap.values());

        while (!toBeAdded.isEmpty()) {
            if (stack.empty()) {
                stack.push(toBeAdded.iterator().next());
            }

            Module<? extends G> top = stack.peek();
            boolean canAdd = true;

            List<Module<? extends G>> rels = relations.get(top);
            if (rels != null) {
                for (Module<? extends G> rel : rels) {
                    if (toBeAdded.contains(rel)) {
                        if (stack.contains(rel)) {
                            circularDependencies.add(rel.id());
                        } else {
                            stack.push(rel);
                            canAdd = false;
                        }
                    }
                }
            }

            if (canAdd) {
                stack.pop();
                toBeAdded.remove(top);
                modules.add(top);
                loaded.add(top.id());
            }
        }
    }

    void sort() {
        missing.clear();
        optMissing.clear();
        modules.clear();
        setupRelations();
        reorder();
    }

    Set<NSID> circularDependencies() {
        return circularDependencies;
    }

    Set<NSID> missing() {
        return missing;
    }

    Set<NSID> optMissing() {
        return optMissing;
    }

    Set<NSID> loaded() {
        return loaded;
    }

    List<Module<? extends G>> modules() {
        return modules;
    }
}
