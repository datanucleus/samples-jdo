# JDO : Dynamic Class/MetaData Generation, Enhancement and Runtime Using DataNucleus

It is possible with DataNucleus to [dynamically generate classes in-memory](#Class_Generation), then [generate MetaData for this in-memory class](#MetaData_Generation), 
then to [enhance the in-memory class definition](#Enhancement), and subsequently to [generate the Schema for this class](#SchemaTool), and finally to [persist objects](#Persistence). 
All of this without any physical _"class"_ file. To do so you need to make use of a custom ClassLoader, and the use the various APIs for JDO3+.

_Note : The current version of this code can be found in_
[GitHub](https://github.com/datanucleus/tests/blob/master/jdo/general/src/test/org/datanucleus/tests/DynamicEnhanceSchemaToolTest.java).


## Class Generation

This step is needed where we don't actually have any Java class file(s) to persist. If you have your Java class then skip this

    private static byte[] createClass(String className)
    throws Exception 
    {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        FieldVisitor fv;

        String classNameASM = className.replace('.', '/');

        // TODO Use getAsmVersionForJRE instead of V1_6 (requires proper stack map)
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, classNameASM, null, 
            "java/lang/Object", new String[]{});

        fv = cw.visitField(Opcodes.ACC_PRIVATE, "name", "Ljava/lang/String;", null, null);
        fv.visitEnd();

        // Default Constructor
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(Opcodes.RETURN);

            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + classNameASM + ";", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // String getName()
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, classNameASM, "name", "Ljava/lang/String;");
            mv.visitInsn(Opcodes.ARETURN);

            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + classNameASM + ";", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // void setName(String)
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setName", "(Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitFieldInsn(Opcodes.PUTFIELD, classNameASM, "name", "Ljava/lang/String;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(Opcodes.RETURN);

            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "L" + classNameASM + ";", null, l0, l2, 0);
            mv.visitLocalVariable("s", "Ljava/lang/String;", null, l0, l2, 1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        // Object getProperty(String)
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getProperty", "(Ljava/lang/String;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitVarInsn(Opcodes.ASTORE, 2);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitLdcInsn("name");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label l2 = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, classNameASM, "name", "Ljava/lang/String;");
            mv.visitVarInsn(Opcodes.ASTORE, 2);
            mv.visitLabel(l2);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitInsn(Opcodes.ARETURN);

            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "L" + classNameASM + ";", null, l0, l4, 0);
            mv.visitLocalVariable("propertyName", "Ljava/lang/String;", null, l0, l4, 1);
            mv.visitLocalVariable("o", "Ljava/lang/Object;", null, l1, l4, 2);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }

        // void setProperty(String, Object)
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setProperty", "(Ljava/lang/String;Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitLdcInsn("name");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
            mv.visitFieldInsn(Opcodes.PUTFIELD, classNameASM, "name", "Ljava/lang/String;");
            mv.visitLabel(l1);
            mv.visitInsn(Opcodes.RETURN);

            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "L" + classNameASM + ";", null, l0, l3, 0);
            mv.visitLocalVariable("propertyName", "Ljava/lang/String;", null, l0, l3, 1);
            mv.visitLocalVariable("value", "Ljava/lang/Object;", null, l0, l3, 2);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }

        return cw.toByteArray();
    }


So here we have used ASM to generate the bytes for a simple Java bean style class. So we now load this into a CustomClassLoader to make it accessible

    byte[] classBytes = createClass(className);
    CustomClassLoader compileCL = new CustomClassLoader(Thread.currentThread().getContextClassLoader());
    compileCL.defineClass("test.Client", classBytes);


## MetaData Generation

To make use of our generated class we need metadata for how it will be persisted. Omit this step if your class has metadata specified via annotations/XML.
We firstly need to obtain a JDOMetadata object to populate. This step is covered later. Once we have the JDOMetadata object we need to populate it, so we use the JDO3 Metadata API.

    private static void populateMetadata(JDOMetadata jdomd)
    {
        PackageMetadata pmd = jdomd.newPackageMetadata("test");
        ClassMetadata cmd = pmd.newClassMetadata("Client");
        cmd.setTable("CLIENT").setDetachable(true).setIdentityType(javax.jdo.annotations.IdentityType.DATASTORE);
        cmd.setPersistenceModifier(javax.jdo.metadata.ClassPersistenceModifier.PERSISTENCE_CAPABLE);
    
        FieldMetadata fmd = cmd.newFieldMetadata("name");
        fmd.setNullValue(javax.jdo.annotations.NullValue.DEFAULT).setColumn("name").setIndexed(true).setUnique(true);
    
        InheritanceMetadata inhmd = cmd.newInheritanceMetadata();
        inhmd.setStrategy(javax.jdo.annotations.InheritanceStrategy.NEW_TABLE);
        DiscriminatorMetadata dmd = inhmd.newDiscriminatorMetadata();
        dmd.setColumn("disc").setValue("Client").setStrategy(javax.jdo.annotations.DiscriminatorStrategy.VALUE_MAP).setIndexed(Indexed.TRUE);
    
        VersionMetadata vermd = cmd.newVersionMetadata();
        vermd.setStrategy(javax.jdo.annotations.VersionStrategy.VERSION_NUMBER).setColumn("version").setIndexed(Indexed.TRUE);
    }

So we took in the JDOMetadata and populated it with a PackageMetadata, following by ClassMetadata, FieldMetadata etc. This follows the structure of the XML form of JDO metadata.


## Enhancement

Now we want to enhance it. So lets use the JDO3 API, firstly getting the enhancer to use, and then the JDOMetadata object we mentioned above.

    // Get our JDO3 enhancer
    JDOEnhancer enhancer = JDOHelper.getEnhancer();
    enhancer.setClassLoader(compileCL);
    
    // Get a new metadata component to define and populate it
    JDOMetadata jdomd = enhancer.newMetadata();
    populateMetadata(jdomd);
    
    // Register metadata with enhancer
    enhancer.registerMetadata(jdomd);
    
    // Enhance the in-memory bytes
    enhancer.addClass(className, classBytes);
    enhancer.enhance();
    byte[] enhancedBytes = enhancer.getEnhancedBytes(className);

So we created an enhancer, and set our custom class loader holding the in-memory class, and registered the metadata with the enhancer. 
Finally we invoked the enhancer, returning the updated (enhanced) bytes for our "class".


## SchemaTool

Ok, so we have an enhanced class definition, and we can create MetaData for our enhanced class. So now we're ready for SchemaTool to generate the schema for the class. 
Firstly we need to load our enhanced class into a classloader to use at runtime.

    // Create our runtime class loader, and load the enhanced class into it
    CustomClassLoader runtimeCL = new CustomClassLoader(Thread.currentThread().getContextClassLoader());
    runtimeCL.defineClass(className, enhancedBytes);

and now we run SchemaTool, using this ClassLoader

    Map props = new HashMap();
    props.put("javax.jdo.PersistenceManagerFactoryClass","org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
    props.put("datanucleus.ConnectionDriverName","org.hsqldb.jdbcDriver");
    props.put("datanucleus.ConnectionURL","jdbc:hsqldb:mem:nucleus");
    props.put("datanucleus.ConnectionUserName","sa");
    props.put("datanucleus.ConnectionPassword","");
    props.put("datanucleus.schema.autoCreateAll","true");
    props.put("datanucleus.schema.autoCreateTables","true");
    props.put("datanucleus.schema.autoCreateColumns","true");
    props.put("datanucleus.primaryClassLoader",runtimeCL);
    props.put("datanucleus.rdbms.stringDefaultLength","255");
    PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
    JDOMetaData jdomd = pmf.newMetadata();
    populateMetadata(jdomd);
    pmf.registerMetadata(jdomd);
    
    List classNames = new ArrayList();
    classNames.add("test.Client");
    
    PersistenceNucleusContext nucCtx = pmf.getNucleusContext();
    StoreManager storeMgr = nucCtx.getStoreManager();
    if (!(storeMgr instanceof SchemaAwareStoreManager))
    {
        // Can't create schema with this datastore
        return;
    }

    try
    {
        SchemaTool schematool = new SchemaTool();
        schematool.setDdlFile("target/schema.ddl").setCompleteDdl(true);
        schematool.createSchemaForClasses((SchemaAwareStoreManager)storeMgr, classNames);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    pmf.close();

So we created a PMF, using our ClassLoader, and then loaded up the MetaData into it. Finally we obtained an instance of SchemaTool, and created the schema.


## Persistence

Persisting objects of the in-memory type is very similar to SchemaTool. We already have our runtime ClassLoader, so we now create a PMF in a similar way and do standard JDO persistence

    Map props = new HashMap();
    props.put("javax.jdo.PersistenceManagerFactoryClass","org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
    props.put("datanucleus.ConnectionDriverName","org.hsqldb.jdbcDriver");
    props.put("datanucleus.ConnectionURL","jdbc:hsqldb:mem:nucleus");
    props.put("datanucleus.ConnectionUserName","sa");
    props.put("datanucleus.ConnectionPassword","");
    props.put("datanucleus.autoStartMechanism","None");
    props.put("datanucleus.schema.autoCreateAll","true");
    props.put("datanucleus.schema.autoCreateTables","true");
    props.put("datanucleus.schema.autoCreateColumns","true");
    props.put("datanucleus.primaryClassLoader ",runtimeCL);
    props.put("datanucleus.rdbms.stringDefaultLength","255");
    PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
    JDOMetadata jdomd = pmf.newMetadata();
    populateMetadata(jdomd);
    pmf.registerMetadata(jdomd);
    
    PersistenceManager pm = pmf.getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    ClassLoaderResolver clr = ((JDOPersistenceManagerFactory)pmf).getNucleusContext().getClassLoaderResolver(runtimeCL);
    {
        tx.begin();
        Class clazz = clr.classForName("test.Client");
        Object o = clazz.newInstance();
        pm.makePersistent(o);
        tx.commit();
    }
    pmf.close();
