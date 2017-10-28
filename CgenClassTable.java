/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;

/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;


<<<<<<< HEAD


/*

***********************************************************************************************************

*/

//unsure if necessary
    private LinkedHashMap<Integer, CgenNode> tagMap;

    //static type -> CgenNode (used in dispatch, maybe other places)
    private LinkedHashMap<AbstractSymbol, CgenNode> nameMap;
	
	  private int labelNum;
	
  	public CgenNode getCgenNodeByName(AbstractSymbol class_name) {
  		return getCgenNode(class_name);
  	}

    public CgenNode getCgenNode(AbstractSymbol name){
      if(name == TreeConstants.SELF_TYPE) return currentClass;
      CgenNode val = nameMap.get(name);
      if(val == null) Utilities.fatalError("returning null value from CgenClassTable.getCgenNode()");
      return val;
    }

    private CgenNode currentClass;

    //in WORDS
    private int SPOffsetFromFP = 1;

/*

***********************************************************************************************************

*/


=======
>>>>>>> 5a5cacf1394c015a7a0a3acdfb66b6ca00aa0a60
    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class = 
	    new class_(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class = 
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class = 
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class = 
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

<<<<<<< HEAD
/************************************************************/
private void installFeatures(){
    	CgenNode object = root();
    	installFeaturesInner(new Vector<MethodPair>(), new Vector<attr>(), object);
    }

    //installs a list of available methods in each node, paired with the class they're associated with.
    //this includes all inherited methods and overrides all .
    private void installFeaturesInner(Vector<MethodPair> inheritedMethods, Vector<attr> inheritedAttrs, CgenNode node){
	  	Vector<MethodPair> methods = new Vector<MethodPair>(inheritedMethods);
      Vector<attr> attrs = new Vector<attr>();
      Vector<MethodPair> newMethods = new Vector<MethodPair>();
      for(Feature f : (ArrayList<Feature>) Collections.list(node.getFeatures().getElements())){
        if(f instanceof method){
          newMethods.add(new MethodPair(node, (method)f));
        } else {
          attrs.add((attr) f);
        }
      }

      //perform overrides in-place to ensure same ordering as in parent
      for(int i = 0; i < methods.size(); i++){
        int found = newMethods.indexOf(methods.get(i));
        if(found != -1){
          methods.set(i, newMethods.get(found));
          newMethods.removeElementAt(found);
        }
      }

      //append whatever was not used in an override
      methods.addAll(newMethods);

	    node.setMethods(methods);
      node.setInheritedAttrs(inheritedAttrs);
      node.setLocalAttrs(attrs);
	    for(CgenNode child : (ArrayList<CgenNode>) Collections.list(node.getChildren())){
        Vector<attr> allAttrs = new Vector<attr>(inheritedAttrs);
        allAttrs.addAll(attrs);
	    	installFeaturesInner(methods, allAttrs, child);
	    }
    }

    private void codeClassNameTab() {
    	str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
    	for(int i = 0; i < nds.size(); i++){
    		CgenNode node = nds.get(i);
    		node.setClassTag(i);
        tagMap.put(i, node);
        nameMap.put(node.name, node);
 	   		str.print(CgenSupport.WORD);
        ((StringSymbol) AbstractTable.stringtable.lookup(node.name.toString())).codeRef(str);
        str.println();
 	   	}
    }

    //TODO: this outputs the classes in a different order than the reference.
    //I don't THINK this that's bad?
    private void codeClassObjTab(){
    	str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
    	for(CgenNode node : nds){
    		str.print(CgenSupport.WORD);
    		CgenSupport.emitProtObjRef(node.name, str);
    		str.println();
    		str.print(CgenSupport.WORD);
    		CgenSupport.emitInitRef(node.name, str);
    		str.println();
    	}
    }

    private void codeDispTab(CgenNode node){
    	str.print(node.name.toString() + CgenSupport.DISPTAB_SUFFIX + CgenSupport.LABEL);
    	for(MethodPair pair : node.getMethods()){
    		str.print(CgenSupport.WORD);
    		CgenSupport.emitMethodRef(pair.cnode.name, pair.met.name, str);
    		str.println();
    	}
    }

    private void codeDispTabs(){
    	for(CgenNode node : nds){
    		codeDispTab(node);
    	}
    }

    private void codeAttr(attr a){
      str.print(CgenSupport.WORD);
      AbstractSymbol klass = a.type_decl;
      if(klass == TreeConstants.Int){
        ((IntSymbol) AbstractTable.inttable.lookup("0")).codeRef(str);
      } else if (klass == TreeConstants.Bool){
        BoolConst.falsebool.codeRef(str);
      } else if (klass == TreeConstants.Str){
        ((StringSymbol) AbstractTable.stringtable.lookup("")).codeRef(str);
      } else {
        str.print(0); // void
      }
      str.println();
    }

    private void codeProtObj(CgenNode klass){
    	str.println(CgenSupport.WORD + "-1");
    	CgenSupport.emitProtObjRef(klass.name, str);
    	str.print(CgenSupport.LABEL);
    	str.println(CgenSupport.WORD + klass.getClassTag());
      Vector<attr> attrs = klass.getAttrs();
      int objSize = 3 + attrs.size();
      str.println(CgenSupport.WORD + objSize);
      str.print(CgenSupport.WORD);
      CgenSupport.emitDispTableRef(klass.name, str);
      str.println();
      for(attr a : attrs){
        codeAttr(a);
      }
    }

    private void codeProtObjs() {
    	for (CgenNode node : nds){
	    	codeProtObj(node);
	    }
    }

    private void codeInit(CgenNode klass){

      enterScope();
      Vector<attr> attrs = klass.getAttrs();
      for (int i = 0; i < attrs.size(); i++) {
      //assuming attrs were added in order
        Location newLoc = new Location();
        newLoc.register = CgenSupport.SELF; //we DO have a self object in initializers
        newLoc.offset = 3 + i;
        this.addId(attrs.get(i).name, newLoc);
      }

      //boilerplate
      str.print(klass.name + CgenSupport.CLASSINIT_SUFFIX + CgenSupport.LABEL);
      CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -12, str);
      CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, str);
      CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, str);
      CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, str);
      CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str);
      CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str); //callee saves acc
      AbstractSymbol parent = klass.getParent();
      if(parent != TreeConstants.No_class){
        str.print(CgenSupport.JAL);
        str.print(parent + CgenSupport.CLASSINIT_SUFFIX);
        str.println();
      }

      //evaluate and store local attrs
      for(attr a : klass.getNonInheritedAttrs()){
        if(a.init instanceof no_expr) continue;
		    setCurrentClass(klass);
        a.init.code(str, this);
        CgenSupport.emitStore(CgenSupport.ACC, klass.getAttrOffset(a.name), CgenSupport.SELF, str);
		    if(Flags.cgen_Memmgr == 1){
            CgenSupport.emitAddiu(CgenSupport.A1, CgenSupport.SELF, 4 * klass.getAttrOffset(a.name), str);
            CgenSupport.emitGCAssign(str);    
        }
        setCurrentClass(null);
      }

      //boilerplate
      CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str); //callee restores acc
      CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
      CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, str);
      CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
      CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 12, str);
      CgenSupport.emitReturn(str);

      exitScope();
    }

    private void codeInitializers(){
      for (CgenNode node : nds){
        codeInit(node);
      }
    }




/***********************************************************/
=======
>>>>>>> 5a5cacf1394c015a7a0a3acdfb66b6ca00aa0a60
    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	stringclasstag = 0 /* Change to your String class tag here */;
	intclasstag =    0 /* Change to your Int class tag here */;
	boolclasstag =   0 /* Change to your Bool class tag here */;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

<<<<<<< HEAD
	/********************************************/

	installFeatures();
	/*********************************************/

=======
>>>>>>> 5a5cacf1394c015a7a0a3acdfb66b6ca00aa0a60
	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	//                 Add your code to emit
	//                   - prototype objects
	//                   - class_nameTab
	//                   - dispatch tables

<<<<<<< HEAD
			codeClassNameTab();

			codeClassObjTab();

			codeDispTabs();

			//Generate object prototypes (including starter classes)
			codeProtObjs();

			if (Flags.cgen_debug) System.out.println("coding global text");
			codeGlobalText();

      codeInitializers();



=======
>>>>>>> 5a5cacf1394c015a7a0a3acdfb66b6ca00aa0a60
	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	//                   - the class methods
	//                   - etc...
    }

    /** Gets the root of the inheritance tree */
<<<<<<< HEAD
    public CgenNode root() 
    {
	return (CgenNode)probe(TreeConstants.Object_);
    }

public int nextLabel(){
      labelNum++;
      return labelNum;
    }

    public void setCurrentClass(CgenNode node){
      currentClass = node;
    }

    public CgenNode getCurrentClass(){
      if (currentClass == null) Utilities.fatalError("returning null value from CgenClassTable.getCurrentClass(). "
        + "I'm assuming this should never happen until I'm proven otherwise.");
      return currentClass;
    }

    public int getSPOffsetFromFP(){
      return -SPOffsetFromFP;
    }

    //called when entering method
    public void resetSPOffsetFromFP(){
      SPOffsetFromFP = 1;
    }

    public void emitUncountedPush(String reg, PrintStream s){
      CgenSupport.emitStore(reg, 0, CgenSupport.SP, s);
      CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
    }

    public void emitPush(String reg, PrintStream s) {
      SPOffsetFromFP++;
      emitUncountedPush(reg, s);
    }

    public void emitUncountedPop(PrintStream s) {
      if(SPOffsetFromFP < 1) Utilities.fatalError("too much popping!! CgenClassTable.emitPop()");
      CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
    }

    public void emitPop(PrintStream s) {
      SPOffsetFromFP--;
      emitUncountedPop(s);
    }

    public void emitUncountedPopR(String destRegister, PrintStream s){
      CgenSupport.emitLoad(destRegister, 1, CgenSupport.SP, s);
      emitUncountedPop(s);
    }

    public void emitPopR(String destRegister, PrintStream s) {
      CgenSupport.emitLoad(destRegister, 1, CgenSupport.SP, s);
      emitPop(s);
    }

    public void emitStoreDefaultValue(String destRegister, AbstractSymbol klass, PrintStream str){
      if(klass == TreeConstants.Int){
        IntSymbol intDef = (IntSymbol) AbstractTable.inttable.lookup("0");
        CgenSupport.emitLoadInt(destRegister, intDef, str);
      } else if (klass == TreeConstants.Bool){
        CgenSupport.emitLoadBool(destRegister, BoolConst.falsebool, str);
      } else if (klass == TreeConstants.Str){
        StringSymbol strDef = (StringSymbol) AbstractTable.stringtable.lookup("");
        CgenSupport.emitLoadString(destRegister, strDef, str);
      } else {
        CgenSupport.emitLoadImm(destRegister, 0, str); // void
      }
    }


    
=======
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
>>>>>>> 5a5cacf1394c015a7a0a3acdfb66b6ca00aa0a60
}
			  
    
