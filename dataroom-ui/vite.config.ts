import {fileURLToPath, URL} from 'node:url'
import path from 'path'
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import ElementPlus from 'unplugin-element-plus/vite'
import {ReplaceThisPluginType} from './ReplaceThisPluginType'

// https://vite.dev/config/
export default defineConfig({
  base: './',
  define: {
    __THIS_PLUGIN_TYPE__: JSON.stringify(process.env.THIS_PLUGIN_TYPE || ''),
  },
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver({ importStyle: 'sass' })],
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: 'sass' })]
    }),
    ElementPlus({ useSource: true }),
    ReplaceThisPluginType(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@DrPackage': fileURLToPath(new URL('./src/packages', import.meta.url))
    },
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/element.scss" as *;`
      },
    },
  },
})
