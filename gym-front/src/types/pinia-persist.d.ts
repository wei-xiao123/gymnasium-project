import 'pinia'
import type { PiniaPluginContext } from 'pinia'

declare module 'pinia' {
  export interface DefineStoreOptionsBase<S, Store> {
    persist?: {
      enabled?: boolean
      strategies?: Array<{
        storage?: Storage
        paths?: string[]
      }>
    }
  }
}
