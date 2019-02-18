#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_testprocesses_org_getprocess_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_testprocesses_org_getprocess_MainActivity_passProcessInfos(JNIEnv *env, jobject /* this */, jobject processList)
{
   jclass jListClass = env->GetObjectClass(processList);
   jmethodID jListGet = env->GetMethodID(jListClass,"get","(I)Ljava/lang/Object;");
   jmethodID jListSize = env->GetMethodID(jListClass,"size","()I");
   jint len = env->CallIntMethod(processList,jListSize);
    std::string result = "";
    for (int i = 0; i < len; ++i)
    {
           jobject procInfo = env->CallObjectMethod(processList,jListGet,i);
           jclass jProcInfoClass = env->GetObjectClass(procInfo);

           jmethodID jGetAppName = env->GetMethodID(jProcInfoClass,"GetAppName","()Ljava/lang/String;");
           jmethodID jGetPackageName = env->GetMethodID(jProcInfoClass,"GetPackageName","()Ljava/lang/String;");
           jstring jstrAppName = (jstring)env->CallObjectMethod(procInfo,jGetAppName);
           jstring jstrPackageName = (jstring)env->CallObjectMethod(procInfo,jGetPackageName);

           const char* pszAppName = env->GetStringUTFChars(jstrAppName,0);
           const char* pszPackageName = env->GetStringUTFChars(jstrPackageName,0);
           std::string strAppName(pszAppName);
           std::string strPackageName(pszPackageName);
            result += strAppName;
            result += "\n";
            //result += strPackageName;
    }
    return env->NewStringUTF(result.c_str());
}