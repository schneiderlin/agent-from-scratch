(ns com.linzihao.web.svg.icon
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   [hyperfiddle.electric-svg3 :as svg]))

(e/defn DoubleLeftArrow []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "arrowChevronDoubleBackward"
               :style {:width "22px" :height "22px" :display "block" :fill "inherit" :flex-shrink 0}})
   (svg/path (dom/props {:d "M3.608 10.442a.625.625 0 0 1 0-.884l5.4-5.4a.625.625 0 0 1 .884.884L4.934 10l4.958 4.958a.625.625 0 1 1-.884.884z"}))
   (svg/path (dom/props {:d "m14.508 4.158-5.4 5.4a.625.625 0 0 0 0 .884l5.4 5.4a.625.625 0 1 0 .884-.884L10.434 10l4.958-4.958a.625.625 0 1 0-.884-.884"}))))

(e/defn Compose []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "compose"
               :style {:width "22px" :height "22px" :display "block" :fill "#322C2C" :flex-shrink 0}})
   (svg/path (dom/props {:d "m16.774 4.341-.59.589-1.109-1.11.596-.594a.784.784 0 0 1 1.103 0c.302.302.302.8 0 1.102zM8.65 12.462l6.816-6.813-1.11-1.11-6.822 6.808a1.1 1.1 0 0 0-.236.393l-.289.932c-.052.196.131.38.315.314l.932-.288a.9.9 0 0 0 .394-.236"}))
   (svg/path (dom/props {:d "M4.375 6.25c0-1.036.84-1.875 1.875-1.875H11a.625.625 0 1 0 0-1.25H6.25A3.125 3.125 0 0 0 3.125 6.25v7.5c0 1.726 1.4 3.125 3.125 3.125h7.5c1.726 0 3.125-1.4 3.125-3.125V9a.625.625 0 1 0-1.25 0v4.75c0 1.036-.84 1.875-1.875 1.875h-7.5a1.875 1.875 0 0 1-1.875-1.875z"}))))

(e/defn Search []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M8.5 3.5a5 5 0 1 1 0 10 5 5 0 0 1 0-10zm7 13-3.5-3.5" :strokeWidth "1.5" :strokeLinecap "round" :strokeLinejoin "round"}))))

(e/defn Home []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M3 9.5 10 4l7 5.5V16a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" :strokeWidth "1.5" :strokeLinejoin "round"}))))

(e/defn Inbox []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M3.5 6.5v7a2 2 0 0 0 2 2h9a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2h-9a2 2 0 0 0-2 2zm0 7 3.5-3 2.5 2 2.5-2 3.5 3" :strokeWidth "1.5" :strokeLinejoin "round"}))))

(e/defn Document []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M6 2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V7.828A2 2 0 0 0 15.414 7L13 4.586A2 2 0 0 0 11.586 4H6zm0 0h5a1 1 0 0 1 1 1v3a1 1 0 0 0 1 1h3M6 2v16m8-8H6" :strokeWidth "1.5" :strokeLinejoin "round" :strokeLinecap "round"}))))

(e/defn Chevron
  ([] (Chevron 0))
  ([rotation]
   (svg/svg
    (dom/props {:viewBox "0 0 20 20"
                :class "chevronDownRoundedThick"
                :style {:width "18px"
                        :height "18px"
                        :display "block"
                        :fill "rgba(55,53,47,0.35)"
                        :transform (str "rotate(" rotation "deg)")
                        :transition "transform 200ms cubic-bezier(0.4,0,0.2,1)"}})
    (svg/path (dom/props {:d "M10.0456 14.6712C10.4524 14.6712 10.7861 14.5247 11.1035 14.1911L17.1338 8.0387C17.378 7.7946 17.5 7.4935 17.5 7.1354C17.5 6.4193 16.9139 5.8333 16.2061 5.8333C15.8477 5.8333 15.5138 5.9798 15.2539 6.2484L10.0537 11.6113L4.8454 6.2484C4.5768 5.9798 4.2513 5.8333 3.8851 5.8333C3.169 5.8333 2.5911 6.4193 2.5911 7.1354C2.5911 7.4935 2.7132 7.7946 2.9574 8.0387L8.9876 14.1911C9.3131 14.5329 9.6468 14.6712 10.0456 14.6712Z"})))))

(e/defn Ellipsis []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :class "ellipsisSmall" :style {:width "18px" :height "18px" :display "block" :fill "rgba(71,70,68,0.6)"}})
   (svg/path (dom/props {:d "M3.2 6.725a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55m4.8 0a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55m4.8 0a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55"}))))

(e/defn Plus []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :class "plusSmall" :style {:width "18px" :height "18px" :display "block" :fill "rgba(71,70,68,0.6)"}})
   (svg/path (dom/props {:d "M8 2.74a.66.66 0 0 1 .66.66v3.94h3.94a.66.66 0 0 1 0 1.32H8.66v3.94a.66.66 0 0 1-1.32 0V8.66H3.4a.66.66 0 0 1 0-1.32h3.94V3.4A.66.66 0 0 1 8 2.74"}))))

(e/defn Star []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "star"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flex-shrink 0}})
   (svg/path (dom/props {:d "M10 2.399c.27 0 .51.174.594.432l1.507 4.636h4.874a.625.625 0 0 1 .367 1.13L13.4 11.462l1.506 4.636a.625.625 0 0 1-.962.699L10 13.932l-3.943 2.865a.625.625 0 0 1-.962-.699L6.6 11.462 2.658 8.597a.625.625 0 0 1 .367-1.13h4.874L9.406 2.83A.625.625 0 0 1 10 2.399m0 2.647L8.948 8.285a.625.625 0 0 1-.595.432H4.95l2.754 2c.22.16.31.442.227.7l-1.052 3.238 2.755-2.001a.625.625 0 0 1 .734 0l2.755 2-1.052-3.237a.625.625 0 0 1 .227-.7l2.754-2h-3.405a.625.625 0 0 1-.594-.432z"}))))

(e/defn Link []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "link"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flex-shrink 0}})
   (svg/path (dom/props {:d "M10.61 3.61a3.776 3.776 0 0 1 5.34 0l.367.368a3.776 3.776 0 0 1 0 5.34l-1.852 1.853a.625.625 0 1 1-.884-.884l1.853-1.853a2.526 2.526 0 0 0 0-3.572l-.368-.367a2.526 2.526 0 0 0-3.572 0L9.641 6.347a.625.625 0 1 1-.883-.883z"}))))

(e/defn Duplicate []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "duplicate"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flex-shrink 0}})
   (svg/path (dom/props {:d "M4.5 2.375A2.125 2.125 0 0 0 2.375 4.5V12c0 1.174.951 2.125 2.125 2.125h1.625v1.625c0 1.174.951 2.125 2.125 2.125h7.5a2.125 2.125 0 0 0 2.125-2.125v-7.5a2.125 2.125 0 0 0-2.125-2.125h-1.625V4.5A2.125 2.125 0 0 0 12 2.375zm8.375 3.75H8.25A2.125 2.125 0 0 0 6.125 8.25v4.625H4.5A.875.875 0 0 1 3.625 12V4.5c0-.483.392-.875.875-.875H12c.483 0 .875.392.875.875zm-5.5 2.125c0-.483.392-.875.875-.875h7.5c.483 0 .875.392.875.875v7.5a.875.875 0 0 1-.875.875h-7.5a.875.875 0 0 1-.875-.875z"}))))
(e/defn Trash []
  (svg/svg 
   (dom/props {:ariaHidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "trash"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "inherit"
                       :flexShrink 0}})

   (svg/path (dom/props {:d "M8.806 8.505a.55.55 0 0 0-1.1 0v5.979a.55.55 0 1 0 1.1 0zm3.488 0a.55.55 0 0 0-1.1 0v5.979a.55.55 0 1 0 1.1 0z"}))
   (svg/path (dom/props {:d "M6.386 3.925v1.464H3.523a.625.625 0 1 0 0 1.25h.897l.393 8.646A2.425 2.425 0 0 0 7.236 17.6h5.528a2.425 2.425 0 0 0 2.422-2.315l.393-8.646h.898a.625.625 0 1 0 0-1.25h-2.863V3.925c0-.842-.683-1.525-1.525-1.525H7.91c-.842 0-1.524.683-1.524 1.525M7.91 3.65h4.18c.15 0 .274.123.274.275v1.464H7.636V3.925c0-.152.123-.275.274-.275m-.9 2.99h7.318l-.39 8.588a1.175 1.175 0 0 1-1.174 1.122H7.236a1.175 1.175 0 0 1-1.174-1.122l-.39-8.589z"}))))

(e/defn ArrowSquarePathUpDown []
  (svg/svg
   (dom/props {:ariaHidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "arrowSquarePathUpDown"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flexShrink 0}})

   (svg/path (dom/props {:d "M6.475 3.125a.625.625 0 1 0 0 1.25h7.975c.65 0 1.175.526 1.175 1.175v6.057l-1.408-1.408a.625.625 0 1 0-.884.884l2.475 2.475a.625.625 0 0 0 .884 0l2.475-2.475a.625.625 0 0 0-.884-.884l-1.408 1.408V5.55a2.425 2.425 0 0 0-2.425-2.425zM3.308 6.442a.625.625 0 0 1 .884 0l2.475 2.475a.625.625 0 1 1-.884.884L4.375 8.393v6.057c0 .649.526 1.175 1.175 1.175h7.975a.625.625 0 0 1 0 1.25H5.55a2.425 2.425 0 0 1-2.425-2.425V8.393L1.717 9.801a.625.625 0 1 1-.884-.884z"}))))

(e/defn ArrowDiagonalUpRight []
  (svg/svg
   (dom/props {:ariaHidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "arrowDiagonalUpRight"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flexShrink 0}})

   (svg/path (dom/props {:d "M15.575 5.05a.625.625 0 0 0-.625-.625H7.313a.625.625 0 1 0 0 1.25h6.128L4.596 14.52a.617.617 0 0 0 .012.872c.244.244.635.25.872.012l8.845-8.845v6.128a.625.625 0 1 0 1.25 0z"}))))

(e/defn PeekSide []
  (svg/svg
   (dom/props {:ariaHidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "peekSide"
               :style {:width "20px"
                       :height "20px"
                       :display "block"
                       :fill "rgb(50, 48, 44)"
                       :flexShrink 0}})

   (svg/path (dom/props {:d "M10.392 6.125a.5.5 0 0 0-.5.5v6.75a.5.5 0 0 0 .5.5h4.683a.5.5 0 0 0 .5-.5v-6.75a.5.5 0 0 0-.5-.5z"}))
   (svg/path (dom/props {:d "M4.5 4.125A2.125 2.125 0 0 0 2.375 6.25v7.5c0 1.174.951 2.125 2.125 2.125h11a2.125 2.125 0 0 0 2.125-2.125v-7.5A2.125 2.125 0 0 0 15.5 4.125zM3.625 6.25c0-.483.392-.875.875-.875h11c.483 0 .875.392.875.875v7.5a.875.875 0 0 1-.875.875h-11a.875.875 0 0 1-.875-.875z"}))))