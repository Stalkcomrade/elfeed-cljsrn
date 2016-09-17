(ns elfeed-cljsrn.android.scenes.entry
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [elfeed-cljsrn.rn :as rn]
            [elfeed-cljsrn.ui :as ui :refer [colors palette button icon]]
            [elfeed-cljsrn.events]
            [elfeed-cljsrn.subs]))

(defn remote-error-message []
  [rn/view {:style {:padding 10
                    :background-color "#fff9c4"}}
   [rn/text "Network error. Check your wifi or your elfeed server."]])

(defn tag [label]
  (let [styles {:wrapper {:background-color (:grey300 colors)
                          :margin-left 4
                          :padding-vertical 2
                          :padding-horizontal 4}
                :text {:font-size 12
                       :color (:primary-text colors)}}]
    [rn/view {:style (:wrapper styles)}
     [rn/text {:style (:text styles)} label]]))

(defn entry-scene [entry]
  (let [loading? (subscribe [:fetching-entry?])
        remote-error (subscribe [:remote-error :entry])
        entry-content (subscribe [:current-entry])
        styles {:wrapper {:flex 1}
                :header {:margin-bottom 10
                         :padding-vertical 10
                         :padding-horizontal 10
                         :border-bottom-color (:divider palette)
                         :border-bottom-width 1}
                :feed-info {:flex 1
                            :flex-direction "row"}
                :loading-content {:flex 1
                                  :padding-left 10
                                  :justify-content "center"
                                  :align-items "center"}
                :content {}
                :web-view {:height 600}}
        ;; content-height (r/atom 200)
        ]
    (fn [entry]
      [rn/view {:style (:wrapper styles)}
       (when @remote-error
         [remote-error-message])
       [rn/view {:style (:header styles)}
        [rn/text (:title @entry-content)]
        [rn/view {:style (:feed-info styles)}
         [rn/text (str "» " (:title (:feed @entry-content)))]
         (for [tag-label (:tags @entry-content)] ^{:key tag-label} [tag tag-label])]]
       [rn/view {:style (if @loading? (:loading-content styles) (:content styles))}
        (if @loading?
          [rn/activity-indicator]
          [rn/web-view {:style (:web-view styles)
                        ;; :injectedJavaScript "document.body.scrollHeight;"
                        ;; :onNavigationStateChange (fn [event]
                        ;;                            (println (str "HOLA: " (.-jsEvaluationValue event)))
                        ;;                            (reset! content-height (.-jsEvaluationValue event))
                        ;;                            )
                        :javaScriptEnabled true
                        :scrollEnabled false
                        :automaticallyAdjustContentInsets true
                        :source {:html (:content-body @entry-content)}}])]])))
