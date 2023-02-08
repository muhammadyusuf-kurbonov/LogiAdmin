package uz.qmgroup.logiadmin.utils

//
//suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
//    addOnSuccessListener { continuation.resume(it) }
//        .addOnCanceledListener { continuation.cancel() }
//        .addOnFailureListener {
//            continuation.cancel(it)
//        }
//}