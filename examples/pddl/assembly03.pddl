
(DEFINE (PROBLEM ASSEM-1)
   (:DOMAIN ASSEMBLY)
   (:OBJECTS HOOZAWHATSIE PLUG HACK DOODAD WIRE TUBE VALVE COIL
             FASTENER GIMCRACK KLUDGE - ASSEMBLY
             CLAMP HAMMER - RESOURCE)
   (:INIT (AVAILABLE HACK)
          (AVAILABLE DOODAD)
          (AVAILABLE WIRE)
          (AVAILABLE TUBE)
          (AVAILABLE VALVE)
          (AVAILABLE FASTENER)
          (AVAILABLE GIMCRACK)
          (AVAILABLE KLUDGE)
          (REQUIRES PLUG HAMMER)
          (REQUIRES COIL CLAMP)
          (PART-OF PLUG HOOZAWHATSIE)
          (PART-OF COIL HOOZAWHATSIE)
          (PART-OF HACK PLUG)
          (PART-OF DOODAD PLUG)
          (PART-OF WIRE PLUG)
          (PART-OF TUBE PLUG)
          (PART-OF VALVE PLUG)
          (PART-OF FASTENER COIL)
          (PART-OF GIMCRACK COIL)
          (PART-OF KLUDGE COIL)
          (ASSEMBLE-ORDER COIL PLUG HOOZAWHATSIE)
          (ASSEMBLE-ORDER DOODAD HACK PLUG)
          (ASSEMBLE-ORDER WIRE HACK PLUG)
          (ASSEMBLE-ORDER VALVE DOODAD PLUG))
   (:GOAL (COMPLETE HOOZAWHATSIE)))
