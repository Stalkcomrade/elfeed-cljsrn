(ns elfeed-cljsrn.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :entries
 (fn [db _]
   (:entries db)))

(reg-sub
 :recent-reads
 (fn [db _]
   (:recent-reads db)))

(reg-sub
 :loading?
 (fn [db]
   (and (:loading-remotely? db)
        (not (:loading-locally? db))
        (empty? (:entries db)))))

(reg-sub
 :fetching-entry?
 (fn [db]
   (:fetching-entry? db)))

(reg-sub
 :current-entry
 (fn [db]
   (let [id (:current-entry db)]
     (get (:entries-m db) id))))

(reg-sub
 :server
 (fn [db]
   (:server db)))

(reg-sub
 :android-drawer
 (fn [db]
   (:android-drawer db)))

(reg-sub
 :remote-error
 (fn [db [_ key]]
   ((keyword (str "error-" (name key))) db)))

(reg-sub
 :update-time
 (fn [db]
   (:update-time db)))
