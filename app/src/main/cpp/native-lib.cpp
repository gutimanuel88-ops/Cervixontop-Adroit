#include <jni.h>
#include <string>
#include <vector>
#include <cstdlib>
#include "node.h"

extern "C" JNIEXPORT jint JNICALL
Java_com_cervixontop_mobile_NodeRuntime_startNode(JNIEnv* env, jclass, jobjectArray args) {
    const jsize argc = env->GetArrayLength(args);
    std::vector<std::string> storage;
    storage.reserve(argc);
    std::vector<char*> argv;
    argv.reserve(argc + 1);

    for (jsize i = 0; i < argc; ++i) {
        auto jstr = (jstring)env->GetObjectArrayElement(args, i);
        const char* utf = env->GetStringUTFChars(jstr, nullptr);
        storage.emplace_back(utf ? utf : "");
        if (utf) env->ReleaseStringUTFChars(jstr, utf);
        env->DeleteLocalRef(jstr);
    }
    for (auto& s : storage) argv.push_back(s.data());
    argv.push_back(nullptr);

    return node::Start(static_cast<int>(argc), argv.data());
}
