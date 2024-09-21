-keepattributes *Annotation*

-keepclassmembers class **$Companion {
    public static ** serializer();
}

-keepclassmembers class ** {
    private static final kotlinx.serialization.KSerializer[] $childSerializers;
}

-keep class ** implements kotlinx.serialization.internal.GeneratedSerializer {
    *;
}